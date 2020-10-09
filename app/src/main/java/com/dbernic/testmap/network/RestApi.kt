package com.dbernic.testmap.network

import com.dbernic.testmap.network.Request
import com.dbernic.testmap.utils.Constants
import retrofit2.Call
import retrofit2.http.*

interface RestApi {

    @POST("route")
    @Headers("Content-Type: application/json")
    fun getRoute(@Query("key") key: String, @Body body: Request): Call<Result>
}
