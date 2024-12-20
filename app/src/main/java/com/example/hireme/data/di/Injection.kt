package com.example.hireme.data.di

import android.content.Context
import com.example.hireme.data.Repository
import com.example.hireme.data.api.ApiConfig
import com.example.hireme.data.local.pref.Preference
import com.example.hireme.data.local.pref.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = Preference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService, pref)
    }
}