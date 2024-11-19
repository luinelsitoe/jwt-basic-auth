package com.luinel.notas_kt

import com.luinel.notas_kt.dto.LoginRequestDto
import com.luinel.notas_kt.dto.LoginResponseDto
import com.luinel.notas_kt.dto.RegisterRequestDto
import com.luinel.notas_kt.dto.RegisterResponseDto
import com.luinel.notas_kt.service.JwtService
import com.luinel.notas_kt.service.AuthenticationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/auth")
@RestController
class AuthenticationController(
    private val jwtService: JwtService,
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/signup")
    fun register(@RequestBody registerUserDto: RegisterRequestDto): ResponseEntity<RegisterResponseDto> {
        val registeredUser = authenticationService.signup(registerUserDto)
        val response = RegisterResponseDto(
            registeredUser.email,
            registeredUser.password
        )

        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun authenticate(@RequestBody loginUserDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        val authenticatedUser = authenticationService.authenticate(loginUserDto)
        val jwtToken = jwtService.generateToken(authenticatedUser)
        val loginResponse = LoginResponseDto().apply {
            token = jwtToken
            expiresIn = jwtService.getExpirationTime()
        }
        return ResponseEntity.ok(loginResponse)
    }
}
