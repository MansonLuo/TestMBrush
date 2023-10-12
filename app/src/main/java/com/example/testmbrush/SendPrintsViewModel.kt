package com.example.testmbrush

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.testmbrush.api.usecases.SendSinglePrintUseCase

class SendPrintsViewModel(private val sendSinglePrintUseCase: SendSinglePrintUseCase): ViewModel() {
    val sendResult = mutableStateOf<String?>(null)

    suspend fun SendSinglePrint(mbdFilePath: String) {
        resetState()

        val res = sendSinglePrintUseCase(mbdFilePath).status
        Log.d("Main", "send status: $res")
        sendResult.value = res
    }

    fun resetState() {
        sendResult.value = null
    }
}