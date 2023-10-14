package com.example.testmbrush.api

import com.example.testmbrush.api.models.Status
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface MBrushService {
    @GET("cgi-bin/cmd?cmd=rm_upload")
    suspend fun removeUpload(): Status

    @Multipart
    @POST("cgi-bin/upload")
    suspend fun upload(
        @Part file: MultipartBody.Part
    ): Status
}