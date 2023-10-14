package com.example.testmbrush.extensions

import android.content.Context
import android.graphics.Bitmap
import com.cherryleafroad.kmagick.Magick
import java.io.File
import java.io.FileOutputStream

class ContextExt {
    companion object {
        var magick: Magick? = null

        fun Context.getMagick(): Magick {
            if (magick == null) {
                magick = Magick.initialize()
            }

            return magick!!
        }

        fun Context.deleteTmpRgbFile(filePath: String) {
            File(filePath).delete()
        }

        fun Context.deleteAllMbdFile(rootPath: String) {
            val rootDir = File(rootPath)

            try {
                rootDir.listFiles()?.forEach {
                    it.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}