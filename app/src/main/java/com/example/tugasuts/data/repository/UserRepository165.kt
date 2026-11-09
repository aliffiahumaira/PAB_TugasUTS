package com.example.tugasuts.data.repository

import com.example.tugasuts.data.local.UserDao165
import com.example.tugasuts.data.model.User165

class UserRepository165(private val userDao165: UserDao165) {

    suspend fun registerUser165(user: User165): Long {
        return userDao165.insertUser(user)
    }

    suspend fun loginUser165(username: String, password: String): User165? {
        val user = userDao165.getUserByUsername(username)
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }

    suspend fun getUserById165(userId: Long): User165? {
        return userDao165.getUserById(userId)
    }
}
