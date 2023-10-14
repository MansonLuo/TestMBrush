package com.example.testmbrush

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.DisposableEffect
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
    val context = LocalContext.current
    val viewModel = remember {
        val vm = SendPrintsViewModel(mbrushRepository)
        vm.loadRootPath(context = context)

        vm
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

        val rootPath = remember {
            context.getExternalFilesDir("images")?.absolutePath
        }
        val tmpRgbFilePath = remember {
            rootPath + File.separator + "tmp.rgb"
        }
        val mbdFilePath = remember {
            rootPath + File.separator + "0.mbd"
        }


        viewModel.imageUri.value?.let { uri ->
            GlideImage(model = uri, contentDescription = null)
        }

        viewModel.sendResult.value?.let { status ->
            Text(text = status)
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

                    scope.launch {
                        viewModel.resetState()
                        viewModel.transformText(
                            text,
                            context = context
                        )

                        Log.d("Main", "pos in transformText: 正常")
                        viewModel.send()
                    }
                }
            ) {
                Text(text = "生成")
            }
            
            Button(
                onClick = {
                    scope.launch {
                        viewModel.removeUpload(context)
                    }
                }
            ) {
                Text(text = "清空")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    App()
}