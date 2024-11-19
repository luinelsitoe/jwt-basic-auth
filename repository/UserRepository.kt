package com.luinel.notas_kt.repository

import com.luinel.notas_kt.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): Optional<User>
}