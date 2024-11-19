import java.io.File
import java.io.IOException
import java.util.logging.Logger
import kotlin.system.exitProcess

//Path do projecto e nome do pacote principal
val userPathRootOfProject = args[0].removeSuffix("/")
val userPathSrcOfProject = args[1].removeSuffix("/")
val packageName = args[2]


//verificar do user se os dados estao corretos
println("\u001B[31m#####################################################\u001B[0m")
println("\u001B[31m### ATENCAO! TENHA UMA BASE DE DADOS CONFIGURADA! ###\u001B[0m")
println("\u001B[31m####################################################\u001B[0m")
println("Caminho da raiz projecto: $userPathRootOfProject")
println("Caminho da source do projecto: $userPathSrcOfProject")
println("Nome do pacote principal: $packageName")
print("Deseja continuar?(s/n): ")
val char = readln()

if (char != "s")
    exitProcess(0)

//####################################VARIAVEIS DE CAMINHO###########################################
//nomes dos pacotes que seram inseridos nos ficheiros a serem transferidos
val packageNameConfig = "$packageName.config"
val packageNameException = "$packageName.exception"
val packageNameDto = "$packageName.dto"
val packageNameModel = "$packageName.model"
val packageNameSecurity = "$packageName.security"
val packageNameController = "$packageName.controller"
val packageNameService = "$packageName.service"
val packageNameRepository = "$packageName.repository"

//path dos ficheiros a serem transferidos
val scriptPath = "/home/luinel/IdeaProjects/JwtBasicAuth"
val configPath = "$scriptPath/config/AuthenticationConfiguration.kt"
val exceptionPath = "$scriptPath/exception/AuthenticationExceptionHandler.kt"
val dtoPath = "$scriptPath/dto/AuthenticationDto.kt"
val modelPath = "$scriptPath/model/User.kt"
val securityFilterPath = "$scriptPath/security/JwtAuthenticationFilter.kt"
val securityConfigPath = "$scriptPath/security/SecurityConfiguration.kt"
val controllerUserPath = "$scriptPath/controller/UserController.kt"
val controllerAuthPath = "$scriptPath/controller/AuthenticationController.kt"
val serviceAuthPath = "$scriptPath/service/AuthenticationService.kt"
val serviceJwtPath = "$scriptPath/service/JwtService.kt"
val serviceUserPath = "$scriptPath/service/UserService.kt"
val repositoryUserPath = "$scriptPath/repository/UserRepository.kt"
val properties = "$scriptPath/properties.txt"
val dependencies = "$scriptPath/dependencies.txt"

//construir ficheiros a serem transferidos
var configFile = File(configPath)
var exceptionFile = File(exceptionPath)
var dtoFile = File(dtoPath)
var modelFile = File(modelPath)
var securityFilterFile = File(securityFilterPath)
var securityConfigFile = File(securityConfigPath)
var controllerUserFile = File(controllerUserPath)
var controllerAuthFile = File(controllerAuthPath)
var serviceAuthFile = File(serviceAuthPath)
var serviceJwtFile = File(serviceJwtPath)
var serviceUserFile = File(serviceUserPath)
var repositoryUserFile = File(repositoryUserPath)
var propertiesFile = File(properties)
var dependenciesFile = File(dependencies)

//construir ficheiros do projecto do user
val userSrcOfProjectDir = File(userPathSrcOfProject)
val userConfigDir = File("$userPathSrcOfProject/config")
val userExceptionDir = File("$userPathSrcOfProject/exception")
val userDtoDir = File("$userPathSrcOfProject/dto")
val userModelDir = File("$userPathSrcOfProject/model")
val userSecurityDir = File("$userPathSrcOfProject/security")
val userControllerDir = File("$userPathSrcOfProject/controller")
val userServiceDir = File("$userPathSrcOfProject/service")
val userRepositoryDir = File("$userPathSrcOfProject/repository")
val userBuildGradleFile = File("$userPathRootOfProject/build.gradle.kts")
val userPropertiesFile = File("$userPathRootOfProject/src/main/resources/application.properties")
//###########################################################################################


//############################################Fluxo do script#################################
prepararProjecto()

copiarFiles()

substituirAllPackages()

appendBuildFile()

appendProperties()

val processBuilder = ProcessBuilder()

processBuilder.directory(File(userPathRootOfProject))
processBuilder.command("./gradlew", "build")

try {
    val process = processBuilder.start()
    val exit = process.waitFor()

    if (exit == 0)
        Log.logger.info("Build executado com sucesso\n")
    else
        Log.logger.severe("Falha no build. Possivel problema: falta de uma configuração de bases de dados\n")
} catch (e: IOException) {

}

Log.logger.info("Script terminado!")

//############################################################################################

//##############################################Funcoes#######################################
fun prepararProjecto() {
    if (!userSrcOfProjectDir.exists())
        throw Exception("source do projecto nao existe")

    if (!userBuildGradleFile.exists())
        throw Exception("build.gradle.kts nao existe")

    if (!userPropertiesFile.exists())
        throw Exception("application.properties nao existe")

    if (!File("$userPathRootOfProject/gradlew").exists())
        throw Exception("O script do gradlew nao existe")

    if (!userServiceDir.exists())
        userServiceDir.mkdir()

    if (!userControllerDir.exists())
        userControllerDir.mkdir()

    if (!userSecurityDir.exists())
        userSecurityDir.mkdir()

    if (!userModelDir.exists())
        userModelDir.mkdir()

    if (!userDtoDir.exists())
        userDtoDir.mkdir()

    if (!userExceptionDir.exists())
        userExceptionDir.mkdir()

    if (!userConfigDir.exists())
        userConfigDir.mkdir()

    if (!userRepositoryDir.exists())
        userRepositoryDir.mkdir()

    Log.logger.info("Diretorios necessarios criados")
}

fun appendBuildFile() {
    val linesUser = userBuildGradleFile.readLines().toMutableList()
    val newLines = dependenciesFile.readLines()
        .filter { it !in linesUser }

    val dependenciesAdded = insertLine(newLines, linesUser).joinToString("\n")

    userBuildGradleFile.writeText(dependenciesAdded)
    Log.logger.info("Dependencias escritas")
}

fun insertLine(linesToInsert: List<String>, linesWhereToInsert: MutableList<String>): MutableList<String> {

    for (i in linesWhereToInsert.indices) {
        if (linesWhereToInsert[i].contains("dependencies")) {
            linesWhereToInsert.addAll(i + 1, linesToInsert)
            break
        }
    }
    return linesWhereToInsert
}

fun appendProperties() {

    var count = 0
    for (line in propertiesFile.readLines()) {
        if (userPropertiesFile.readText().contains(line)) {
            count++
            if (count == 3) {
                Log.logger.info("Propriedades exsitentes. Sem necessidade de sobrescrever")
                return
            }
        }
    }

    for (line in propertiesFile.readLines()) {
        userPropertiesFile.appendText(line + "\n")
    }

    Log.logger.info("Propriedades escritas")
}

fun substituirPackage(file: File, packge: String) {
    val lines = file.readLines()
    val newLines = mutableListOf<String>()

    for (line in lines) {
        var updatedLine = line

        if (updatedLine.contains("com.luinel.notas_kt.model"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.model", "$packageName.model")
        else if (updatedLine.contains("com.luinel.notas_kt.repository"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.repository", "$packageName.repository")
        else if (updatedLine.contains("com.luinel.notas_kt.service"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.service", "$packageName.service")
        else if (updatedLine.contains("com.luinel.notas_kt.config"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.config", "$packageName.config")
        else if (updatedLine.contains("com.luinel.notas_kt.controller"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.controller", "$packageName.controller")
        else if (updatedLine.contains("com.luinel.notas_kt.security"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.security", "$packageName.security")
        else if (updatedLine.contains("com.luinel.notas_kt.exception"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.exception", "$packageName.exception")
        else if (updatedLine.contains("com.luinel.notas_kt.dto"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt.dto", "$packageName.dto")
        else if (updatedLine.contains("com.luinel.notas_kt"))
            updatedLine = updatedLine.replace("com.luinel.notas_kt", "$packge")

        newLines.add(updatedLine)
    }

    file.writeText(newLines.joinToString("\n"))
}


fun substituirAllPackages() {
    substituirPackage(configFile, packageNameConfig)
    substituirPackage(exceptionFile, packageNameException)
    substituirPackage(dtoFile, packageNameDto)
    substituirPackage(modelFile, packageNameModel)
    substituirPackage(securityFilterFile, packageNameSecurity)
    substituirPackage(securityConfigFile, packageNameSecurity)
    substituirPackage(controllerUserFile, packageNameController)
    substituirPackage(controllerAuthFile, packageNameController)
    substituirPackage(serviceJwtFile, packageNameService)
    substituirPackage(serviceUserFile, packageNameService)
    substituirPackage(serviceAuthFile, packageNameService)
    substituirPackage(repositoryUserFile, packageNameRepository)

    Log.logger.info("Nomes dos pacotes trocados")
}

/*
Copia os ficheiros para o projecto do user e troca os valores das variaveis para os ficheiros novos.
Fiz isso porque ja havia implementado a logica da substituicao
Feio sim mas é pratico
*/
fun copiarFiles() {
    repositoryUserFile = repositoryUserFile.copyTo(File(userRepositoryDir, repositoryUserFile.name), true)
    serviceUserFile = serviceUserFile.copyTo(File(userServiceDir, serviceUserFile.name), true)
    serviceJwtFile = serviceJwtFile.copyTo(File(userServiceDir, serviceJwtFile.name), true)
    serviceAuthFile = serviceAuthFile.copyTo(File(userServiceDir, serviceAuthFile.name), true)
    controllerAuthFile = controllerAuthFile.copyTo(File(userControllerDir, controllerAuthFile.name), true)
    controllerUserFile = controllerUserFile.copyTo(File(userControllerDir, controllerUserFile.name), true)
    securityConfigFile = securityConfigFile.copyTo(File(userSecurityDir, securityConfigFile.name), true)
    securityFilterFile = securityFilterFile.copyTo(File(userSecurityDir, securityFilterFile.name), true)
    modelFile = modelFile.copyTo(File(userModelDir, modelFile.name), true)
    dtoFile = dtoFile.copyTo(File(userDtoDir, dtoFile.name), true)
    exceptionFile = exceptionFile.copyTo(File(userExceptionDir, exceptionFile.name), true)
    configFile = configFile.copyTo(File(userConfigDir, configFile.name), true)

    Log.logger.info("Ficheiros necessarios copiados")
}
//############################################################################################

//Logger do java
class Log {
    companion object {
        val logger = Logger.getLogger(Log::class.java.name)
    }
}

