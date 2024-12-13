package com.example.hireme.ui.login

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireme.data.Repository
import com.example.hireme.data.api.response.LoginResponse
import com.example.hireme.data.local.model.User
import com.example.hireme.data.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch {
            repository.login(email, password)
        }
    }

    fun clear() {
        viewModelScope.launch {
            repository.clearLoginResult()
        }
    }

    val getLoginResult: MediatorLiveData<Result<LoginResponse>?> = repository.getLoginResult()

    fun saveSession(user: User) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}