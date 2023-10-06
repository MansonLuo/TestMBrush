package com.example.testmbrush

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.drawToBitmap
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.cherryleafroad.kmagick.FilterType
import com.cherryleafroad.kmagick.Magick
import com.cherryleafroad.kmagick.MagickWand
import com.cherryleafroad.kmagick.PixelWand
import com.example.testmbrush.ui.theme.TestMBrushTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun App(msg: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        var imgUri by remember {
            mutableStateOf<Uri?>(null)
        }

        val galleryLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetMultipleContents()
        ) { list ->
            imgUri = list.first()
        }

        if (imgUri != null) {
            GlideImage(
                model = imgUri,
                contentDescription = ""
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    resizeImage(imgUri!!.path!!)
                }
            ) {
                Text(text = msg)
            }
            
            Button(
                onClick = {
                    galleryLauncher.launch("image/*")
                }
            ) {
                Text(text = "取图")         
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App(msg = "ss")
}

fun resizeImage(imgPath: String) {
    Magick.initialize().use {
        val a = MagickWand()
        a.readImage(imgPath)
        val w = a.getImageWidth()
        val h = a.getImageHeight()

        a.resizeImage(w / 2, h / 2, FilterType.LanczosFilter)
        a.imageCompressionQuality = 95
        a.writeImage(imgPath)
    }
}