package com.example.hireme.ui.login

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hireme.R
import com.example.hireme.data.local.model.User
import com.example.hireme.databinding.ActivityLoginBinding
import com.example.hireme.ui.ViewModelFactory
import com.example.hireme.ui.main.MainActivity
import com.example.hireme.ui.register.RegisterActivity
import com.example.hireme.data.Result

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
        setupRememberMe()
    }

    private fun setupAction() {
        viewModel.getLoginResult.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data

                        val name = data.loginResult?.name
                        val email = binding.etLoginEmail.text.toString()
                        val password = binding.etLoginPassword.text.toString()
                        val token = data.loginResult?.token

                        if (name != null && token != null) {
                            viewModel.saveSession(User(name, email, token, true))
                            rememberMe(email, password)
                            showSuccessMessage()
                        }
                        viewModel.clear()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showFailedMessage(result.error)
                        viewModel.clear()
                    }
                }
            }
        }

        binding.btnLoginSubmit.setOnClickListener {
            val email = binding.etLoginEmail.text.toString()
            val password = binding.etLoginPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_login_fields), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.login(email, password)
            }
        }

        // Link Register
        binding.tvLoginRegisterLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showSuccessMessage() {
        if (Build.VERSION.SDK_INT >= 30) {
            val toast = Toast.makeText(this, getString(R.string.success_login), Toast.LENGTH_SHORT)
            toast.addCallback(object : Toast.Callback() {
                override fun onToastHidden() {
                    super.onToastHidden()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
            })
            toast.show()
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.success))
                setMessage(getString(R.string.success_login))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        }
    }

    private fun showFailedMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupRememberMe() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        if (sharedPreferences.getBoolean("remember_me", false)) {
            val savedEmail = sharedPreferences.getString("user_email", "")
            val savedPassword = sharedPreferences.getString("user_password", "")
            binding.etLoginEmail.setText(savedEmail)
            binding.etLoginPassword.setText(savedPassword)
            binding.cbLoginRememberMe.isChecked = true
        }
    }

    private fun rememberMe(email: String, password: String) {
        if (binding.cbLoginRememberMe.isChecked) {
            val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("user_email", email)
            editor.putString("user_password", password)
            editor.putBoolean("remember_me", true)
            editor.apply()
        }
    }
}