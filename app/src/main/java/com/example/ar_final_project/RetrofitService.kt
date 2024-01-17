package com.example.ar_final_project

import com.example.ar_final_project.model.Product
import com.example.ar_final_project.model.RemoteResult
import com.example.ar_final_project.model.UploadProduct
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

data class LoginResponse(val token: String? = null, val nonFieldErrors: List<String>? = null)
data class SignUpResponse(val id: Int, val username: String)
data class UriFromApi(val modeluri: String)
data class ResponseId(val id: Int)
interface RetrofitService {
    @FormUrlEncoded
    @POST("signup/")
    suspend fun signup(
        @Field("username") username: String,
        @Field("password") password: String
    ): SignUpResponse
    @FormUrlEncoded
    @POST("login/")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): LoginResponse
    @POST("logout/")
    suspend fun logout(@Header("Authorization") token: String?): Response<Void>
    @DELETE("delete_user/{user}/")
    suspend fun deleteUser(@Path("user") userId: String?): Response<Void>
    object RetrofitServiceFactory {
        fun makeRetrofitService(): RetrofitService {
            return Retrofit.Builder().baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(RetrofitService::class.java)
        }
    }
}
interface UploadRetrofitService {
    @POST("api/products/")
    @Multipart
    suspend fun createProduct(
        @Part("name") name: RequestBody,
        @Part("price") price: RequestBody,
        @Part img: MultipartBody.Part?,
        @Part model: MultipartBody.Part?): Response<UploadProduct>
    companion object Factory {
        private val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY  // Log body of requests and responses
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        fun makeRetrofitService(): UploadRetrofitService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(UploadRetrofitService::class.java)
        }
    }
}
interface ProductRetrofitService {
    @GET("get_user_id/{user}/")
    suspend fun userId(@Path("user") userId: String?): ResponseId
    @GET("api/products/")
    suspend fun searchItems(@Query("search") query: String): RemoteResult
    @GET("api/user_products/")
    suspend fun myProducts(@Query("user") query: String): RemoteResult
    @GET("api/products/")
    suspend fun products(): RemoteResult
    @GET("api/products/{productId}/")
    suspend fun getProduct(@Path("productId") productId: String): Product
    @PUT("api/buy/{productId}/")
    suspend fun buyProduct(@Path("productId") productId: String): Product
    object ProductRetrofitServiceFactory {
        fun makeRetrofitService(): ProductRetrofitService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductRetrofitService::class.java)
        }
    }
}
interface ModelRetrofitService {
    @GET("api/get_3d_model/{productId}/")
    suspend fun downloadModel(@Path("productId") productId: String): UriFromApi
    object ModelRetrofitServiceFactory {
        fun makeRetrofitService(): ModelRetrofitService {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ModelRetrofitService::class.java)
        }
    }
}