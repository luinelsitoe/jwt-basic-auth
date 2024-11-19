package com.luinel.notas_kt

import com.luinel.notas_kt.dto.LoginRequestDto
import com.luinel.notas_kt.dto.RegisterRequestDto
import com.luinel.notas_kt.model.User
import com.luinel.notas_kt.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val authenticationManager: AuthenticationManager,
    private val passwordEncoder: PasswordEncoder
) {

    fun signup(input: RegisterRequestDto): User {
        val user = User().apply {
            fullName = input.fullName
            email = input.email
            password = passwordEncoder.encode(input.password)
        }

        return userRepository.save(user)
    }

    fun authenticate(input: LoginRequestDto): User {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(input.email, input.password)
        )

        return userRepository.findByEmail(input.email)
            .orElseThrow { IllegalArgumentException("User not found") }
    }
}
