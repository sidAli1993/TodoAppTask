package com.sid_ali_tech.todoapptask.presentation.navigations

sealed class NavScreen(val route : String){
    object SplashScreen : NavScreen("splash_screen")
    object HomeScreen : NavScreen("home_screen")
    object AddEditTaskScreen : NavScreen("addedit_screen")
}
