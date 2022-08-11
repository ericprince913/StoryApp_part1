package com.example.bpaai_submission1.Retrofit

import com.example.bpaai_submission1.Response.AddNewStoryResponse
import com.example.bpaai_submission1.Response.AllStoryResponse
import com.example.bpaai_submission1.Response.LoginResponse
import com.example.bpaai_submission1.Response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>

    @GET("stories")
    fun getStory(
        @Header("Authorization") header: String
    ) : Call<AllStoryResponse>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") header: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<AddNewStoryResponse>
}