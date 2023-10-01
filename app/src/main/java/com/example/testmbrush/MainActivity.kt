package com.example.testmbrush

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import com.example.testmbrush.ui.theme.TestMBrushTheme
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
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
                    App()
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun App() {
    val context = LocalContext.current

    val snapShot = CaptureBitmap {
        Button(
            onClick = {},
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(text = "Capture my imag")
        }
    }

    Column {
        val uri by remember {
            mutableStateOf<Uri?>(null)
        }

        if (uri != null) {
            GlideImage(
                model = uri,
                contentDescription = ""
            )
        }

        Button(
            onClick = {
                MainScope().launch {
                    val bitmap = snapShot.invoke()
                    val url = Tool.saveImage(bitmap, context = context)
                }
            },
            modifier = Modifier
                .padding(24.dp)
        ) {
            Text(text = "生成")
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ShowNumbers(
    numbers: String,
    modifier: Modifier = Modifier
) {
    val textMeasure = rememberTextMeasurer()

    Spacer (
        modifier = modifier
            .drawWithCache {
                val measuredText = textMeasure.measure(
                    AnnotatedString(numbers),
                    constraints = Constraints.fixedWidth((size.width * 2f / 3f).toInt()),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                onDrawBehind {
                    drawRect(
                        Color.White, size = measuredText.size.toSize()
                    )
                    drawText(
                        measuredText,
                        topLeft = Offset(x = 20.dp.toPx(), y = 0f)
                    )
                }
            }
            .fillMaxWidth()
            .height(50.dp)
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        var text by remember {
            mutableStateOf("填入数字")
        }

        ShowNumbers(numbers = text)
        TextField(
            value = text,
            onValueChange = {
                text = it
            }
        )
        Button(
            onClick = {
            }
        ) {
           Text(text = "生成图片")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    Content()
}

@Composable
fun CaptureBitmap(
    content: @Composable () -> Unit
): () -> Bitmap {
    val context = LocalContext.current

    val composeView = remember {
        ComposeView(context)
    }

    fun captureBitmap(): Bitmap {
        return composeView.drawToBitmap()
    }

    AndroidView(
        factory = {
            composeView.apply {
                setContent {
                    content.invoke()
                }
            }
        }
    )

    return ::captureBitmap
}