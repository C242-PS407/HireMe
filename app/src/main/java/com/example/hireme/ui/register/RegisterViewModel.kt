package com.example.hireme.ui.register

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hireme.data.Repository
import com.example.hireme.data.Result
import com.example.hireme.data.api.response.SignupResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            repository.register(name, email, password)
        }
    }

    fun clear() {
        viewModelScope.launch {
            repository.clearRegisterResult()
        }
    }

    val getRegisterResult: MediatorLiveData<Result<SignupResponse>?> = repository.getRegisterResult()
}