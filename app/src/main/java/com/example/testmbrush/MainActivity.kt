package com.example.testmbrush

import android.net.Uri
import android.os.Bundle
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.testmbrush.api.RetrofitInstance
import com.example.testmbrush.api.MbrushRepository
import com.example.testmbrush.api.usecases.SendSinglePrintUseCase
import com.example.testmbrush.extensions.ContextExt.Companion.deleteTmpRgbFile
import com.example.testmbrush.extensions.saveJpgTo
import com.example.testmbrush.extensions.transformAndSaveToTmpRgb
import com.example.testmbrush.ui.theme.TestMBrushTheme
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : ComponentActivity() {
    external fun generateMBDFile(rgbFilePath: String, mbdFilePath: String)

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

    companion object {
        init {
            System.loadLibrary("testmbrush")
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val mbrushRepository = remember {
        MbrushRepository(RetrofitInstance.mBrushService)
    }
    val sendSinglePrintUseCase = remember {
        SendSinglePrintUseCase(mbrushRepository)
    }
    val viewModel = remember {
        SendPrintsViewModel(sendSinglePrintUseCase)
    }
    val scope = rememberCoroutineScope()

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
        val rootPath = remember {
            context.getExternalFilesDir("images")?.absolutePath
        }
        val tmpRgbFilePath = remember {
            rootPath + File.separator + "tmp.rgb"
        }
        val mbdFilePath = remember {
            rootPath + File.separator + "0.mbd"
        }

        imageUri?.let { uri ->
            GlideImage(model = uri, contentDescription = null)
        }

        viewModel.sendResult.value?.let { status ->
            Text(text = "请求状态: ${status}")
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
                    imageUri = text.saveJpgTo(rootPath!!).let { imgPath ->
                        imgPath.transformAndSaveToTmpRgb(context, rootPath)
                        (context as MainActivity).generateMBDFile(
                            tmpRgbFilePath,
                            mbdFilePath
                        )
                        context.deleteTmpRgbFile(tmpRgbFilePath)
                        Uri.fromFile(File(imgPath))
                    }
                    scope.launch {
                        viewModel.SendSinglePrint(mbdFilePath)
                    }
                }
            ) {
                Text(text = "生成")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}