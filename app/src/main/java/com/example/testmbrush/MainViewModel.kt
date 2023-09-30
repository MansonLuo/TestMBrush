package com.example.testmbrush

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel: ViewModel() {
    var title by mutableStateOf("获取")

    fun loadData(onResult: (String) -> Unit) {
        val call: Call<Resp> = Network.api.getComment()

        call.enqueue(object : Callback<Resp?> {
            override fun onResponse(call: Call<Resp?>, response: Response<Resp?>) {
                val text = response.body()?.data?.content
                onResult(text!!)
            }

            override fun onFailure(call: Call<Resp?>, t: Throwable) {
            }

        })
    }
}