package com.example.tugasuts.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasuts.data.model.Transaction165
import com.example.tugasuts.databinding.ItemTransaction165Binding
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter165(
    private var transactionList: List<Transaction165>,
    private val onEditClick: (Transaction165) -> Unit,
    private val onDeleteClick: (Transaction165) -> Unit
) : RecyclerView.Adapter<TransactionAdapter165.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: ItemTransaction165Binding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction165) {
            binding.tvCategory165.text = transaction.kategori
            binding.tvDescription165.text = transaction.keterangan
            binding.tvDate165.text = transaction.tanggal

            val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
            binding.tvAmount165.text = formatter.format(transaction.nominal)

            binding.btnEdit165.setOnClickListener {
                onEditClick(transaction)
            }

            binding.btnDelete165.setOnClickListener {
                onDeleteClick(transaction)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransaction165Binding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(transactionList[position])
    }

    override fun getItemCount(): Int = transactionList.size

    fun updateData(newList: List<Transaction165>) {
        transactionList = newList
        notifyDataSetChanged()
    }
}
