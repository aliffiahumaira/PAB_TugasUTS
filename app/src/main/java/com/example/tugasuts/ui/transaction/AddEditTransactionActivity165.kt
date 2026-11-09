package com.example.tugasuts.ui.transaction

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasuts.data.local.AppDatabase165
import com.example.tugasuts.data.model.Transaction165
import com.example.tugasuts.data.preference.SessionManager165
import com.example.tugasuts.data.repository.TransactionRepository165
import com.example.tugasuts.databinding.ActivityAddEditTransaction165Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class AddEditTransactionActivity165 : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditTransaction165Binding
    private lateinit var transactionRepository165: TransactionRepository165
    private lateinit var sessionManager165: SessionManager165

    private var transactionId: Long = -1L
    private var selectedDateStr: String = ""

    private val categories165 = arrayOf(
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
        binding = ActivityAddEditTransaction165Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase165.getDatabase(this)
        transactionRepository165 = TransactionRepository165(database.transactionDao165())
        sessionManager165 = SessionManager165(this)

        setupSpinner165()

        transactionId = intent.getLongExtra("EXTRA_TRANSACTION_ID_165", -1L)
        if (transactionId != -1L) {
            binding.tvTitle165.text = "Edit Transaksi"
            val category = intent.getStringExtra("EXTRA_CATEGORY_165") ?: ""
            val amount = intent.getDoubleExtra("EXTRA_AMOUNT_165", 0.0)
            val description = intent.getStringExtra("EXTRA_DESCRIPTION_165") ?: ""
            val date = intent.getStringExtra("EXTRA_DATE_165") ?: getCurrentDate165()

            val spinnerPosition = categories165.indexOf(category)
            if (spinnerPosition >= 0) {
                binding.spinnerCategory165.setSelection(spinnerPosition)
            }
            binding.etAmount165.setText(amount.toString())
            binding.etDescription165.setText(description)
            selectedDateStr = date
            binding.btnDatePicker165.text = date
        } else {
            binding.tvTitle165.text = "Tambah Transaksi Baru"
            selectedDateStr = getCurrentDate165()
            binding.btnDatePicker165.text = selectedDateStr
        }

        binding.btnDatePicker165.setOnClickListener {
            showDatePickerDialog165()
        }

        binding.btnSaveTransaction165.setOnClickListener {
            val category = binding.spinnerCategory165.selectedItem.toString()
            val amountStr = binding.etAmount165.text.toString().trim()
            val description = binding.etDescription165.text.toString().trim()

            if (amountStr.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountStr.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Nominal harus berupa angka positif", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDateStr.isEmpty()) {
                Toast.makeText(this, "Tanggal harus valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = sessionManager165.getCurrentUserId165()

            lifecycleScope.launch(Dispatchers.IO) {
                if (transactionId != -1L) {
                    val updatedTransaction = Transaction165(
                        id = transactionId,
                        userId = userId,
                        kategori = category,
                        nominal = amount,
                        keterangan = description,
                        tanggal = selectedDateStr
                    )
                    transactionRepository165.updateTransaction165(updatedTransaction)
                } else {
                    val newTransaction = Transaction165(
                        userId = userId,
                        kategori = category,
                        nominal = amount,
                        keterangan = description,
                        tanggal = selectedDateStr
                    )
                    transactionRepository165.insertTransaction165(newTransaction)
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddEditTransactionActivity165, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setupSpinner165() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories165)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory165.adapter = adapter
    }

    private fun showDatePickerDialog165() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format(Locale.getDefault(), "%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear)
            selectedDateStr = formattedDate
            binding.btnDatePicker165.text = formattedDate
        }, year, month, day).show()
    }

    private fun getCurrentDate165(): String {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return String.format(Locale.getDefault(), "%02d-%02d-%d", day, month + 1, year)
    }
}
