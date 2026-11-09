package com.example.tugasuts.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasuts.data.local.AppDatabase165
import com.example.tugasuts.data.model.User165
import com.example.tugasuts.data.repository.UserRepository165
import com.example.tugasuts.databinding.ActivityRegister165Binding
import com.example.tugasuts.utils.PasswordUtils165
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegisterActivity165 : AppCompatActivity() {

    private lateinit var binding: ActivityRegister165Binding
    private lateinit var userRepository165: UserRepository165

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegister165Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase165.getDatabase(this)
        userRepository165 = UserRepository165(database.userDao165())

        binding.btnRegister165.setOnClickListener {
            val username = binding.etUsername165.text.toString().trim()
            val password = binding.etPassword165.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val existingUser = database.userDao165().getUserByUsername(username)
                if (existingUser != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity165, "Username sudah terdaftar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val hashedPassword = PasswordUtils165.hashPassword165(password)
                    val newUser = User165(username = username, password = hashedPassword)
                    userRepository165.registerUser165(newUser)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity165, "Registrasi berhasil, silakan login", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }

        binding.tvToLogin165.setOnClickListener {
            finish()
        }
    }
}
