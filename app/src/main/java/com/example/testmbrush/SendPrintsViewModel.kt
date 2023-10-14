package com.example.testmbrush

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.testmbrush.api.MbrushRepository
import com.example.testmbrush.extensions.ContextExt.Companion.deleteAllMbdFile
import com.example.testmbrush.extensions.ContextExt.Companion.deleteTmpRgbFile
import com.example.testmbrush.extensions.saveJpgTo
import com.example.testmbrush.extensions.transformAndSaveToTmpRgb
import java.io.File

class SendPrintsViewModel(private val mbrushRepository: MbrushRepository): ViewModel() {
    val sendResult = mutableStateOf<String?>(null)
    var imageUri = mutableStateOf<Uri?>(null)
    var pos = mutableStateOf(0)
    lateinit var rootImgPath: String
    lateinit var rootMbdPath: String
    lateinit var rootTmpPath: String
    lateinit var tmpRgbFilePath: String

    suspend fun send() {
        val res = mbrushRepository.upload(
            "$rootMbdPath/${pos.value}.mbd",
            pos.value,
        ).status

        sendResult.value = "发送状态: $res"
        pos.value += 1
    }

    suspend fun removeUpload(context: Context) {
        val res = mbrushRepository.removeUpload().status

        // reset
        context.deleteAllMbdFile(rootMbdPath)
        pos.value = 0
        sendResult.value = "清空状态: $res"
    }

    fun transformText(
        text: String,
        context: Context,
    ) {
        text.saveJpgTo(rootImgPath).let { imgFilePath ->
            imgFilePath.transformAndSaveToTmpRgb(context, rootTmpPath)

            (context as MainActivity).generateMBDFile(
                tmpRgbFilePath,
                "$rootMbdPath/${pos.value}.mbd"
            )

            context.deleteTmpRgbFile(tmpRgbFilePath)
            imageUri.value = Uri.fromFile(File(imgFilePath))
        }

    }

    fun resetState() {
        sendResult.value = null
    }

    fun loadRootPath(context: Context) {
        rootImgPath = context.getExternalFilesDir("images")!!.absolutePath
        rootMbdPath = context.getExternalFilesDir("mbds")!!.absolutePath
        rootTmpPath = context.getExternalFilesDir("tmps")!!.absolutePath
        tmpRgbFilePath = "$rootTmpPath/tmp.rgb"
    }
}