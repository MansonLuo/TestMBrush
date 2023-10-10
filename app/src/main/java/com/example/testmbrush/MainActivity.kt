package com.example.testmbrush

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cherryleafroad.kmagick.DrawingWand
import com.cherryleafroad.kmagick.Magick
import com.cherryleafroad.kmagick.MagickWand
import com.cherryleafroad.kmagick.MagickWandException
import com.cherryleafroad.kmagick.PixelWand
import com.example.testmbrush.extensions.saveJpgOf
import com.example.testmbrush.extensions.transformAndSave
import com.example.testmbrush.ui.theme.TestMBrushTheme
import java.io.File


class MainActivity : ComponentActivity() {
    external fun stringFromJNI(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TestMBrushTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //val viewModel by viewModels<MainViewModel>()
                    //App(viewModel)
                    val msg by remember {
                        mutableStateOf(
                            stringFromJNI()
                        )
                    }
                    App(msg)
                }
            }
        }
    }

    companion object {
        init {
            System.loadLibrary("testmbrush")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App(msg: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var text by remember {
            mutableStateOf("")
        }
        var imageUri by remember {
            mutableStateOf<Uri?>(null)
        }
        val context = LocalContext.current

        imageUri?.let { uri ->
            GlideImage(model = uri, contentDescription = null)
        }


        TextField(
            value = text,
            onValueChange = {
                text = it
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    val path = text.saveJpgOf(context, "ori").transformAndSave(context)
                    imageUri = Uri.fromFile(File(path))
                }
            ) {
                Text(text = "生成")
            }
            Button(
                onClick = {
                    /*
                    image?.value?.let {
                        context.saveToDisk(it, "ori")
                    }
                     */
                }
            ) {
                Text(text = "保存")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App(msg = "ss")
}

fun saveImage(context: Context) {
    val pictureRootDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath
    val savedImagePath = pictureRootDir + File.separator + "生成的图片.jpg"
    Log.d("Main", "image saved path: $savedImagePath")
    Magick.initialize().use {
        val wand = MagickWand()

        val pw = PixelWand()
        pw.color = "#ffffff"
        wand.newImage(500, 500, pw)

        val dwand = DrawingWand()
        dwand.fontSize = 40.0
        dwand.font = "Liberation-Sans-Bold"

        val colorWand = PixelWand()
        colorWand.color = "black"
        dwand.fillColor = colorWand
        try {
            wand.annotateImage(dwand, 0.0, 50.0, 0.0, "Some Text")
        } catch (e: MagickWandException) {
            val exc = wand.getException()
            Log.d("Main", "$e.message")
            Log.d("Main", "${exc.message}")
        }

        wand.writeImage(savedImagePath)
    }
}