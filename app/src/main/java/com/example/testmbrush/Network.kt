package com.example.testmbrush

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MBrushService {
    @GET("cgi-bin/cmd?cmd=rm_upload")
    fun remove_upload(): Call<Status>

    @Multipart
    @POST("cgi-bin/upload")
    fun upload(
        @Part file: MultipartBody.Part
    )
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