package com.example.hireme.data

import com.example.hireme.data.model.User
import com.example.hireme.data.pref.Preference
import kotlinx.coroutines.flow.Flow

class Repository private constructor(
    private val userPreference: Preference
) {
    suspend fun saveSession(user: User) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            preference: Preference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(preference)
            }.also { instance = it }
    }
}