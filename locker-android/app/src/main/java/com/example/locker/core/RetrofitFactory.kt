package com.example.locker.core

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitFactory private constructor() {

    val BASE_URL: String = "http://5.160.146.108:8080";

    companion object {
        private var mInstance: RetrofitFactory? = null

        @Synchronized
        fun getInstance(): RetrofitFactory? {
            if (mInstance == null) {
                mInstance = RetrofitFactory()
            }
            return mInstance;
        }
    }

    var mOkHttpClient: OkHttpClient? = null

    init {
        mOkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor())
            .build()
    }

    fun getUserServices(): UserAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(mOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(UserAPI::class.java)
    }
}