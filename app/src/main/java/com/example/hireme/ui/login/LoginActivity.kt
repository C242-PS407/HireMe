package com.example.hireme.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.hireme.data.model.User
import com.example.hireme.databinding.ActivityLoginBinding
import com.example.hireme.ui.ViewModelFactory
import com.example.hireme.ui.main.MainActivity
import com.example.hireme.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginSubmit.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                // Logika login (dummy check)
                if (email == "test@example.com" && password == "123456") {
                    // Simpan pilihan Remember Me di SharedPreferences
                    if (binding.cbLoginRememberMe.isChecked) {
                        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("user_email", email)
                        editor.putString("user_password", password)
                        editor.putBoolean("remember_me", true)
                        editor.apply()
                    }

                    viewModel.saveSession(User(email, "token_example", true))

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Lanjutkan ke halaman utama setelah login sukses
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()  // Menutup halaman login agar pengguna tidak bisa kembali
                } else {
                    Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Link Register
        binding.tvLoginRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Cek jika user memilih Remember Me dan ada data yang tersimpan
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("remember_me", false)) {
            val savedEmail = sharedPreferences.getString("user_email", "")
            val savedPassword = sharedPreferences.getString("user_password", "")
            binding.etLoginEmail.setText(savedEmail)
            binding.etLoginPassword.setText(savedPassword)
            binding.cbLoginRememberMe.isChecked = true
        }
    }
}