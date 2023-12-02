package com.sid_ali_tech.todoapptask.presentation.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import java.time.ZoneId
import java.time.ZonedDateTime

data class HomeUiState(
    val activeButton: String = "todo",
    val hasNetworkAccess: Boolean = false,
)

class HomeViewModel() : ViewModel() {

    var homeUiState by mutableStateOf(HomeUiState())
        private set


    fun onActiveButtonChange(button : String) {
        homeUiState = homeUiState.copy(activeButton = button)
    }
}