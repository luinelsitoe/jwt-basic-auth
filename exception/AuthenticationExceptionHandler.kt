package com.luinel.notas_kt

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ProblemDetail
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AccountStatusException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AuthenticationExceptionHandler {

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(exception: BadCredentialsException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(401)).apply {
            setProperty("description", "The username or password is incorrect")
        }

    @ExceptionHandler(AccountStatusException::class)
    fun handleAccountStatus(exception: AccountStatusException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(403)).apply {
            setProperty("description", "The account is locked")
        }


    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDenied(exception: AccessDeniedException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(403)).apply {
            setProperty("description", "You are not authorized to access this resource")
        }

    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(exception: SignatureException): ProblemDetail =
        ProblemDetail.forStatus(HttpStatusCode.valueOf(403)).apply {
            setProperty("description", "The JWT signature is invalid")
        }


    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(exception: ExpiredJwtException): ProblemDetail =
        ProblemDetail.forStatus(403)
            .apply {
                setProperty("description", "The JWT token has expired")
            }


}
