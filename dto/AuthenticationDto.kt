package com.luinel.notas_kt

data class LoginRequestDto(
    var email: String,
    var password: String
)

data class RegisterRequestDto(
    var email: String,
    var password: String,
    var fullName: String
)

data class LoginResponseDto(
    var token: String,
    var expiresIn: Long,
) {
    constructor() : this("", 0)
}

data class RegisterResponseDto(
    var email: String,
    var password: String
)
