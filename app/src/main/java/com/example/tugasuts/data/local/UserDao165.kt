package com.example.tugasuts.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tugasuts.data.model.User165

@Dao
interface UserDao165 {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User165): Long

    @Query("SELECT * FROM user_table_165 WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User165?

    @Query("SELECT * FROM user_table_165 WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Long): User165?
}
