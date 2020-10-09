package com.dbernic.testmap.network

import com.dbernic.testmap.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RestClient {
    private var REST_CLIENT: RestApi

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.HOST_DEV)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        REST_CLIENT = retrofit.create<RestApi>(RestApi::class.java)
    }

    fun get(): RestApi {
        return REST_CLIENT
    }

}