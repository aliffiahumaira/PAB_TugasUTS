package com.example.tugasuts.data.repository

import com.example.tugasuts.data.local.TransactionDao165
import com.example.tugasuts.data.model.Transaction165
import kotlinx.coroutines.flow.Flow

class TransactionRepository165(private val transactionDao165: TransactionDao165) {

    suspend fun insertTransaction165(transaction: Transaction165) {
        transactionDao165.insertTransaction(transaction)
    }

    suspend fun updateTransaction165(transaction: Transaction165) {
        transactionDao165.updateTransaction(transaction)
    }

    suspend fun deleteTransaction165(transaction: Transaction165) {
        transactionDao165.deleteTransaction(transaction)
    }

    suspend fun deleteTransactionByIdAndUser165(transactionId: Long, userId: Long) {
        transactionDao165.deleteTransactionByIdAndUser(transactionId, userId)
    }

    fun getTransactionsByUserId165(userId: Long): Flow<List<Transaction165>> {
        return transactionDao165.getAllTransactionsByUserId(userId)
    }

    fun getFilteredTransactions165(userId: Long, category: String, searchQuery: String): Flow<List<Transaction165>> {
        return transactionDao165.getFilteredTransactions(userId, category, searchQuery)
    }

    fun getTotalExpenseByUserId165(userId: Long): Flow<Double?> {
        return transactionDao165.getTotalExpenseByUserId(userId)
    }
}
