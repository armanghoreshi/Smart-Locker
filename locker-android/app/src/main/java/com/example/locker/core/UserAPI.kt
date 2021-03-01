package com.example.locker.core

import com.example.locker.model.body.LoginBodyModel
import com.example.locker.model.body.SignUpBodymodel
import com.example.locker.model.response.*
import retrofit2.Call
import retrofit2.http.*

interface UserAPI {

    @GET("/locker/status")
    fun getUserStatus(@Header("token") token: String?): Call<StatusResponseModel>

    @PUT("/locker/open")
    fun openLocker(@Header("token") token: String): Call<OpenResponseModel>


    @PUT("/locker/return")
    fun returnLocker(@Header("token") token: String): Call<ReturnResponseModel>

    @POST("/locker/take")
    fun takeLocker(@Header("token") token: String): Call<TakeResponseModel>

    @POST("/user/login")
    fun loginUser(@Body loginBody: LoginBodyModel): Call<LoginResponseModel>


    @POST("/user/sign_up")
    fun signUPUser(@Body signUpBodymodel: SignUpBodymodel): Call<SignUpResponseModel>

    @GET("/user/info")
    fun getUserInfo(@Header("token") token: String): Call<UserInfoResponseModel>
}