package com.dbernic.testmap.network

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseCallback<T>(val context: Context?): Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        notifyError("Error: " + t.message)
    }

    override fun onResponse(call: Call<T>, r: Response<T>) {
        if (r.body() == null || (r.code()!= 200)){
            try {
                val errorData =  Gson().fromJson(r.errorBody()!!.string(), ServerError::class.java)
                notifyError(errorData.message)
            } catch (e: Throwable){
                notifyError("Server error")
            }

        } else {
            response(r.body()!!)
        }
    }

    open fun notifyError(text: String){
        if (context != null) Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        Log.w("RESTCallback", "Error:  $text")
    }

    abstract fun response(r: T)
}