package com.example.hireme.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.hireme.data.Repository
import com.example.hireme.data.local.model.CV
import com.example.hireme.data.local.model.User
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    fun getSession(): LiveData<User> {
        return repository.getSession().asLiveData()
    }

    fun addCV(name: String, uri: Uri) {
        viewModelScope.launch {
            repository.addCV(
                CV(
                    name,
                    uri.toString()
                )
            )
        }
    }

    fun removeCV() {
        viewModelScope.launch {
            repository.removeCV()
        }
    }

    fun getCV(): LiveData<CV> {
        return repository.getCV().asLiveData()
    }
}