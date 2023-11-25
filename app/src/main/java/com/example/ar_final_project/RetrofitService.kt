package com.example.ar_final_project

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class ApiResponse(val str: String)

interface RetrofitService {
    @FormUrlEncoded
    @POST("login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): ApiResponse
    object RetrofitServiceFactory {
        fun makeRetrofitService(): RetrofitService {
            return Retrofit.Builder().baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService::class.java)
        }
    }
}