package com.sid_ali_tech.todoapptask

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sid_ali_tech.todoapptask.presentation.navigations.Navigation
import com.sid_ali_tech.todoapptask.presentation.screens.addedittask.AddEditTaskViewModel
import com.sid_ali_tech.todoapptask.presentation.screens.home.HomeViewModel
import com.sid_ali_tech.todoapptask.ui.theme.TodoListAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.permissionx.guolindev.PermissionX
import com.sid_ali_tech.todoapptask.common.Constants
import com.sid_ali_tech.todoapptask.ui.theme.MainBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            var addEditTaskViewModel = viewModel(modelClass = AddEditTaskViewModel::class.java)
            val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)

            Constants.AppCycle = true
            TodoListAppTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MainBackground),
                    color = Color.White
                ) {
                    Navigation(
                        addEditTaskViewModel = addEditTaskViewModel,
                        homeViewModel = homeViewModel,
                    )
                }
            }
            val context = LocalContext.current
            val hasAlarmPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.SET_ALARM
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasAlarmPermission) {
                // Request the SET_ALARM permission
                val requestPermissionLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                        if (isGranted) {
                            // Permission is granted. You can now set alarms.
                        } else {
                            // Permission is denied. Handle accordingly.
                        }
                    }

                // Request the permission when the Composable is first drawn
                LaunchedEffect(Unit) {
                    requestPermissionLauncher.launch(Manifest.permission.SET_ALARM)
                }
            } else {
                // Permission is already granted. You can now set alarms.
            }

        }
    }
}