package com.example.tugasuts.utils

import java.security.MessageDigest

object PasswordUtils165 {
    fun hashPassword165(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
