package com.example.hireme.ui.jobs

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.hireme.data.Repository
import com.example.hireme.data.local.model.CV

class JobsViewModel(private val repository: Repository) : ViewModel() {
    fun getCV(): LiveData<CV> {
        return repository.getCV().asLiveData()
    }
}