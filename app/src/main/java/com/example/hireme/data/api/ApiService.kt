package com.example.hireme.data.api

import com.example.hireme.data.api.request.LoginRequest
import com.example.hireme.data.api.request.SignupRequest
import com.example.hireme.data.api.response.LoginResponse
import com.example.hireme.data.api.response.SignupResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @POST("signup")
    suspend fun signup(@Body signupRequest: SignupRequest) : SignupResponse

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest) : LoginResponse
}