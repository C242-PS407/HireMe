package com.example.hireme.data.di

import android.content.Context
import com.example.hireme.data.Repository
import com.example.hireme.data.pref.Preference
import com.example.hireme.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = Preference.getInstance(context.dataStore)
        return Repository.getInstance(pref)
    }
}