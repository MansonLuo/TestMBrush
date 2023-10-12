package com.example.testmbrush.api

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class MbrushRepository(private val apiService: MBrushService) {
    suspend fun removeUpload(): Status {
        return apiService.removeUpload()
    }

    suspend fun upload(mbdFilePath: String): Status {
        val file = File(mbdFilePath)

        val fileReq = RequestBody.create(MediaType.parse("*"), file)
        val part = MultipartBody.Part.createFormData("file", "0.mbd", fileReq)

        return apiService.upload(part)
    }
}