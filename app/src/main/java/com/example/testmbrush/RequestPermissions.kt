package com.example.testmbrush

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequireExternalPermissions(
    navigateToSettingsScreen: () -> Unit,
    content: @Composable () -> Unit
) {
    val doNotShowRationale by rememberSaveable {
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
    }
}