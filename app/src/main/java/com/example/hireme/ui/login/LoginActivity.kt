package com.example.hireme.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hireme.databinding.ActivityLoginBinding
import com.example.hireme.ui.main.MainActivity
import com.example.hireme.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {
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

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Lanjutkan ke halaman utama setelah login sukses
                    val intent = Intent(this, MainActivity::class.java)
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