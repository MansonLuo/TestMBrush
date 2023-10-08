package com.example.testmbrush

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequireExternalPermissions(
    navigateToSettingsScreen: () -> Unit,
    content: @Composable () -> Unit
) {
    var doNotShowRationale by rememberSaveable {
        mutableStateOf(false)
    }

    val permissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )

    when {
        permissionsState.allPermissionsGranted -> {
            content()
        }

        permissionsState.shouldShowRationale ||
                permissionsState.revokedPermissions.isEmpty() -> {
                    if (doNotShowRationale) {
                        Text(text = "Feature not available")
                    } else {
                        Text(text = "Need to read external storage to import photos. Please grant the permission.")
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Button(onClick = {permissionsState.launchMultiplePermissionRequest()}) {
                                Text(text = "Request permissions")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(onClick = { doNotShowRationale = true}) {
                                Text(text = "Don't show ratioinale again")                          
                            }
                        }
                    }
                }

        else  -> {
            Column {
                Text(
                    "External storage permission denied. " +
                        "Need to read external storage to import photos. " +
                        "Please grant access on the Setting screen."
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { navigateToSettingsScreen() }) {
                    Text(text = "Open Settings")
                }
            }
        }
    }
}