package com.example.hireme.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.example.hireme.data.api.ApiService
import com.example.hireme.data.api.response.LoginResponse
import com.example.hireme.data.api.response.SignupResponse
import com.example.hireme.data.model.User
import com.example.hireme.data.pref.Preference
import kotlinx.coroutines.flow.Flow
import com.example.hireme.data.Result
import com.example.hireme.data.api.request.LoginRequest
import com.example.hireme.data.api.request.SignupRequest
import com.example.hireme.data.api.response.ErrorResponse
import com.example.hireme.data.model.CV
import com.google.gson.Gson
import retrofit2.HttpException

class Repository private constructor(
    private val apiService: ApiService,
    private val preference: Preference
) {
    private val loginResult = MediatorLiveData<Result<LoginResponse>?>()
    private val registerResult = MediatorLiveData<Result<SignupResponse>?>()

    suspend fun saveSession(user: User) {
        preference.saveSession(user)
    }

    fun getSession(): Flow<User> {
        return preference.getSession()
    }

    suspend fun logout() {
        preference.logout()
    }

    suspend fun addCV(cv: CV) {
        preference.addCV(cv)
    }

    suspend fun removeCV() {
        preference.removeCV()
    }

    fun getCV(): Flow<CV> {
        return preference.getCV()
    }

    private fun postLogin(email: String, password: String): LiveData<Result<LoginResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = LoginRequest(
                email,
                password
            )
            val response = apiService.login(request)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.error
            errorMessage?.let {
                emit(Result.Error(it))
            }
        }
    }

    fun login(email: String, password: String): MediatorLiveData<Result<LoginResponse>?> {
        loginResult.addSource(postLogin(email, password)) { result ->
            loginResult.value = result
        }
        return loginResult
    }

    fun getLoginResult(): MediatorLiveData<Result<LoginResponse>?> = loginResult

    fun clearLoginResult() {
        loginResult.value = null
    }

    private fun signup(name: String, email: String, password: String): LiveData<Result<SignupResponse>> = liveData {
        emit(Result.Loading)
        try {
            val request = SignupRequest(
                name,
                email,
                password
            )
            val response = apiService.signup(request)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            val errorMessage = errorBody.error
            errorMessage?.let {
                emit(Result.Error(it))
            }
        }
    }

    fun register(name: String, email: String, password: String): MediatorLiveData<Result<SignupResponse>?> {
        registerResult.addSource(signup(name, email, password)) { result ->
            registerResult.value = result
        }
        return registerResult
    }

    fun clearRegisterResult() {
        registerResult.value = null
    }

    fun getRegisterResult(): MediatorLiveData<Result<SignupResponse>?> = registerResult

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
            preference: Preference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService, preference)
            }.also { instance = it }
    }
}