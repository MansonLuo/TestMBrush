package com.example.testmbrush

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MBrushService {
    @GET("comments.163?format=json")
    fun getComment(): Call<Resp>
}

class Network {
    companion object {
        private var url = "https://api.uomg.com/api/"
        private var retrofit =
            Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val api = retrofit.create(MBrushService::class.java)
    }
}