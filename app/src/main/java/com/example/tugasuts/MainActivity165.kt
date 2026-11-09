package com.example.tugasuts

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasuts.data.local.AppDatabase165
import com.example.tugasuts.data.preference.SessionManager165
import com.example.tugasuts.data.repository.TransactionRepository165
import com.example.tugasuts.databinding.ActivityMain165Binding
import com.example.tugasuts.ui.auth.LoginActivity165
import com.example.tugasuts.ui.main.TransactionAdapter165
import com.example.tugasuts.ui.transaction.AddEditTransactionActivity165
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class MainActivity165 : AppCompatActivity() {

    private lateinit var binding: ActivityMain165Binding
    private lateinit var sessionManager165: SessionManager165
    private lateinit var transactionRepository165: TransactionRepository165
    private lateinit var transactionAdapter165: TransactionAdapter165

    private var selectedCategoryFilter165: String = ""
    private var searchQuery165: String = ""

    private val filterCategories165 = arrayOf(
        "Semua Kategori",
        "Makanan",
        "Transportasi",
        "Belanja",
        "Pendidikan",
        "Hiburan",
        "Kesehatan",
        "Lainnya"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager165 = SessionManager165(this)

        if (!sessionManager165.isLoggedIn165()) {
            navigateToLogin165()
            return
        }

        binding = ActivityMain165Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase165.getDatabase(this)
        transactionRepository165 = TransactionRepository165(database.transactionDao165())

        setupUI165()
        observeData165()
    }

    override fun onResume() {
        super.onResume()
        if (sessionManager165.isLoggedIn165()) {
            observeData165()
        }
    }

    private fun setupUI165() {
        val username = sessionManager165.getCurrentUsername165() ?: "User"
        binding.tvGreeting165.text = "Halo, $username!"

        // Setup Filter Spinner
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterCategories165)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterCategory165.adapter = spinnerAdapter

        binding.spinnerFilterCategory165.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategoryFilter165 = if (position == 0) "" else filterCategories165[position]
                observeData165()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Setup Search Bar
        binding.etSearch165.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchQuery165 = s.toString().trim()
                observeData165()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        transactionAdapter165 = TransactionAdapter165(
            emptyList(),
            onEditClick = { transaction ->
                val intent = Intent(this, AddEditTransactionActivity165::class.java).apply {
                    putExtra("EXTRA_TRANSACTION_ID_165", transaction.id)
                    putExtra("EXTRA_CATEGORY_165", transaction.kategori)
                    putExtra("EXTRA_AMOUNT_165", transaction.nominal)
                    putExtra("EXTRA_DESCRIPTION_165", transaction.keterangan)
                    putExtra("EXTRA_DATE_165", transaction.tanggal)
                }
                startActivity(intent)
            },
            onDeleteClick = { transaction ->
                AlertDialog.Builder(this)
                    .setTitle("Hapus Transaksi")
                    .setMessage("Apakah Anda yakin ingin menghapus transaksi '${transaction.keterangan}'?")
                    .setPositiveButton("Ya") { _, _ ->
                        val userId = sessionManager165.getCurrentUserId165()
                        lifecycleScope.launch(Dispatchers.IO) {
                            transactionRepository165.deleteTransactionByIdAndUser165(transaction.id, userId)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@MainActivity165, "Transaksi berhasil dihapus", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    .setNegativeButton("Batal", null)
                    .show()
            }
        )

        binding.rvTransactions165.apply {
            layoutManager = LinearLayoutManager(this@MainActivity165)
            adapter = transactionAdapter165
        }

        binding.btnAddTransaction165.setOnClickListener {
            val intent = Intent(this, AddEditTransactionActivity165::class.java)
            startActivity(intent)
        }

        binding.btnLogout165.setOnClickListener {
            sessionManager165.clearSession165()
            navigateToLogin165()
        }
    }

    private fun observeData165() {
        val userId = sessionManager165.getCurrentUserId165()

        // Total Expense
        lifecycleScope.launch {
            transactionRepository165.getTotalExpenseByUserId165(userId).collectLatest { total ->
                val totalExpense = total ?: 0.0
                val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
                binding.tvTotalExpense165.text = formatter.format(totalExpense)
            }
        }

        // Filtered Transactions & Count
        lifecycleScope.launch {
            transactionRepository165.getFilteredTransactions165(userId, selectedCategoryFilter165, searchQuery165).collectLatest { list ->
                binding.tvTransactionCount165.text = "Jumlah Transaksi: ${list.size}"
                if (list.isEmpty()) {
                    binding.rvTransactions165.visibility = View.GONE
                    binding.tvEmptyState165.visibility = View.VISIBLE
                } else {
                    binding.rvTransactions165.visibility = View.VISIBLE
                    binding.tvEmptyState165.visibility = View.GONE
                    transactionAdapter165.updateData(list)
                }
            }
        }
    }

    private fun navigateToLogin165() {
        val intent = Intent(this, LoginActivity165::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
