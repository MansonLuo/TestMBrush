package com.example.testmbrush

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream

class Tool {
    companion object {
        fun saveImage(bitmap: Bitmap, context: Context): Uri {
            val file = File(context.filesDir, "image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
            outputStream.close()

            return file.toUri()
        }
    }
}