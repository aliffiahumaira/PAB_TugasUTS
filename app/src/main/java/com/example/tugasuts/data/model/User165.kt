package com.example.tugasuts.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "user_table_165",
    indices = [Index(value = ["username"], unique = true)]
)
data class User165(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val username: String,
    val password: String
)
