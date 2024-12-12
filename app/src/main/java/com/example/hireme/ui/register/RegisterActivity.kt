package com.example.hireme.ui.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hireme.R
import com.example.hireme.data.Result
import com.example.hireme.databinding.ActivityRegisterBinding
import com.example.hireme.ui.ViewModelFactory
import com.example.hireme.ui.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupAction()
    }

    private fun setupAction() {
        viewModel.getRegisterResult.observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val data = result.data
                        viewModel.clear()
                        showSuccessMessage()
                    }
                    is Result.Error -> {
                        showLoading(false)
                        viewModel.clear()
                        showFailedMessage(result.error)
                    }
                }
            }
        }

        binding.btnRegisterSubmit.setOnClickListener {
            val fullName = binding.etRegisterName.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val password = binding.etRegisterPassword.text.toString()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.empty_register_fields), Toast.LENGTH_SHORT).show()
            } else {
                viewModel.register(fullName, email, password)
            }
        }

        binding.tvRegisterLoginLink.setOnClickListener {
            finish()
        }
    }

    private fun showSuccessMessage() {
        if (Build.VERSION.SDK_INT >= 30) {
            val toast = Toast.makeText(this, getString(R.string.success_register), Toast.LENGTH_SHORT)
            toast.addCallback(object : Toast.Callback() {
                override fun onToastHidden() {
                    super.onToastHidden()
                    finish()
                }
            })
            toast.show()
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.success))
                setMessage(getString(R.string.success_register))
                setPositiveButton(getString(R.string.next)) { _, _ ->
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
}