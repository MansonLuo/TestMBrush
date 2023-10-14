package com.example.testmbrush.api.usecases

import com.example.testmbrush.api.MbrushRepository
import com.example.testmbrush.api.models.Status

class SendContinuedPrints(private val mbrushRepository: MbrushRepository) {
    suspend operator fun invoke(pos: Int, mbdFilePath: String): Status {
        return mbrushRepository.upload(mbdFilePath, pos)
    }
}