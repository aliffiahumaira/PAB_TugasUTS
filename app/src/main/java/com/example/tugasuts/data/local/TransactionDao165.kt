package com.example.tugasuts.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tugasuts.data.model.Transaction165
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao165 {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction165)

    @Update
    suspend fun updateTransaction(transaction: Transaction165)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction165)

    @Query("DELETE FROM transaction_table_165 WHERE id = :transactionId AND user_id = :userId")
    suspend fun deleteTransactionByIdAndUser(transactionId: Long, userId: Long)

    @Query("SELECT * FROM transaction_table_165 WHERE user_id = :userId ORDER BY tanggal DESC")
    fun getAllTransactionsByUserId(userId: Long): Flow<List<Transaction165>>

    @Query("SELECT * FROM transaction_table_165 WHERE user_id = :userId AND (:category = '' OR kategori = :category) AND (keterangan LIKE '%' || :searchQuery || '%' OR kategori LIKE '%' || :searchQuery || '%') ORDER BY tanggal DESC")
    fun getFilteredTransactions(userId: Long, category: String, searchQuery: String): Flow<List<Transaction165>>

    @Query("SELECT SUM(nominal) FROM transaction_table_165 WHERE user_id = :userId")
    fun getTotalExpenseByUserId(userId: Long): Flow<Double?>
}
