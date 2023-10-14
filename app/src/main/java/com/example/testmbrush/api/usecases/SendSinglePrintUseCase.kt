package com.example.testmbrush.api.usecases

import com.example.testmbrush.api.MbrushRepository
import com.example.testmbrush.api.models.Status

class SendSinglePrintUseCase(private val mbrushRepository: MbrushRepository) {

    suspend operator fun invoke(mbdFilePath: String): Status {
        val removeStatus = mbrushRepository.removeUpload()

        if (removeStatus.status != "ok") {
            return Status(status = "remove upload failed")
        }

        return mbrushRepository.upload(mbdFilePath)
    }
}