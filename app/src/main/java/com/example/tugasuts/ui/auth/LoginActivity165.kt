package com.example.tugasuts.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.tugasuts.MainActivity165
import com.example.tugasuts.data.local.AppDatabase165
import com.example.tugasuts.data.preference.SessionManager165
import com.example.tugasuts.data.repository.UserRepository165
import com.example.tugasuts.databinding.ActivityLogin165Binding
import com.example.tugasuts.utils.PasswordUtils165
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity165 : AppCompatActivity() {

    private lateinit var binding: ActivityLogin165Binding
    private lateinit var userRepository165: UserRepository165
    private lateinit var sessionManager165: SessionManager165

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin165Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = AppDatabase165.getDatabase(this)
        userRepository165 = UserRepository165(database.userDao165())
        sessionManager165 = SessionManager165(this)

        binding.btnLogin165.setOnClickListener {
            val username = binding.etUsername165.text.toString().trim()
            val password = binding.etPassword165.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val hashedPassword = PasswordUtils165.hashPassword165(password)

            lifecycleScope.launch(Dispatchers.IO) {
                val user = userRepository165.loginUser165(username, hashedPassword)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        sessionManager165.saveUserSession165(user.id, user.username)
                        Toast.makeText(this@LoginActivity165, "Login Berhasil", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity165, MainActivity165::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity165, "Username atau password salah", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.tvToRegister165.setOnClickListener {
            val intent = Intent(this, RegisterActivity165::class.java)
            startActivity(intent)
        }
    }
}
