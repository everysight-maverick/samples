@file:OptIn(ExperimentalPermissionsApi::class)

package com.everysight.sdk.samples.compose

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.everysight.evskit.android.compose.composables.EvsGlassesInfo
import com.everysight.evskit.android.compose.viewmodels.EvsGlassesViewModel
import com.everysight.sdk.samples.compose.ui.theme.EverysightSamplesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EverysightSamplesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize(), contentAlignment = Alignment.Center) {
                        CheckPerm(
                            permissionApproved = {
                                val appViewModel: ComposeViewModel = viewModel()
                                val glassesViewModel: EvsGlassesViewModel = viewModel()
                                if (!appViewModel.wasEvsSdkInitialized) {
                                    Text(stringResource(R.string.evs_sdk_was_not_initialized_yet))
                                } else {
                                    Column(
                                        modifier = Modifier
                                            .padding(innerPadding)
                                            .fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        AnimatedVisibility(glassesViewModel.isReady) {
                                            Text(stringResource(R.string.tap_to_adjust))
                                        }
                                        EvsGlassesInfo()
                                        AnimatedVisibility(glassesViewModel.isReady) {
                                            Text(stringResource(R.string.long_press_to_re_configure))
                                        }
                                    }
                                }
                            },
                            permissionDenied = {
                                Text(stringResource(R.string.approve_permissions_in_settings))
                            }
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun CheckPerm(
    permissionDenied: @Composable BoxScope.() -> Unit,
    permissionApproved: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
) {
    var permissionsRequested = mutableListOf<String>().apply {
        add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            add(android.Manifest.permission.BLUETOOTH_SCAN)
            add(android.Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            add(android.Manifest.permission.BLUETOOTH)
        }
    }
    val permissionsState = rememberMultiplePermissionsState(permissionsRequested)
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}
    LaunchedEffect(!permissionsState.allPermissionsGranted) {
        requestPermissionLauncher.launch(permissionsState.revokedPermissions.map { it.permission }.toTypedArray())
    }
    Box(modifier = modifier) {
        if (permissionsState.allPermissionsGranted) {
            permissionApproved()
        } else {
            permissionDenied()
        }
    }
}