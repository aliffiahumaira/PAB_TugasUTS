package com.example.tugasuts.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tugasuts.data.model.Transaction165
import com.example.tugasuts.data.model.User165

@Database(entities = [User165::class, Transaction165::class], version = 1, exportSchema = false)
abstract class AppDatabase165 : RoomDatabase() {

    abstract fun userDao165(): UserDao165
    abstract fun transactionDao165(): TransactionDao165

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase165? = null

        fun getDatabase(context: Context): AppDatabase165 {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase165::class.java,
                    "expense_tracker_db_165"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
