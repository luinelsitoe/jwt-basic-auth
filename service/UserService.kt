package com.luinel.notas_kt

import com.luinel.notas_kt.model.User
import com.luinel.notas_kt.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun allUsers(): List<User> {
        val users = mutableListOf<User>()

        userRepository.findAll().forEach(users::add)

        return users
    }
}
