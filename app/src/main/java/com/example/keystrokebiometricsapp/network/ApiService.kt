package com.example.keystrokebiometricsapp.network

import com.example.keystrokebiometricsapp.model.KeystrokeSequence
import retrofit2.http.Body
import retrofit2.http.POST

data class RegisterResponse(val message: String)
data class AuthResponse(val score: Double, val authenticated: Boolean)

interface ApiService {
    @POST("register")
    suspend fun register(@Body data: KeystrokeSequence): RegisterResponse

    @POST("authenticate")
    suspend fun authenticate(@Body data: KeystrokeSequence): AuthResponse
}
