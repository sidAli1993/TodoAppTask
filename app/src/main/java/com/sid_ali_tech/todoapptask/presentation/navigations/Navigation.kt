package com.sid_ali_tech.todoapptask.presentation.navigations

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sid_ali_tech.todoapptask.presentation.screens.addedittask.AddEditTaskScreen
import com.sid_ali_tech.todoapptask.presentation.screens.addedittask.AddEditTaskViewModel
import com.sid_ali_tech.todoapptask.presentation.screens.home.HomeScreen
import com.sid_ali_tech.todoapptask.presentation.screens.home.HomeViewModel
import com.sid_ali_tech.todoapptask.presentation.screens.splash.SplashScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState", "SuspiciousIndentation")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(
    navigationController : NavHostController = rememberNavController(),
    addEditTaskViewModel: AddEditTaskViewModel,
    homeViewModel: HomeViewModel,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {

    NavHost(navController = navigationController, startDestination = NavScreen.SplashScreen.route){
        composable(route = NavScreen.SplashScreen.route){
            SplashScreen(navigationController)
        }
        composable(route = NavScreen.HomeScreen.route){
            val scaffoldState : ScaffoldState = rememberScaffoldState()
            HomeScreen(
                navController = navigationController,
                openDrawer = {coroutineScope.launch { scaffoldState.drawerState.open() } },
                scaffoldState = scaffoldState,
                homeViewModel = homeViewModel,
            )
        }
        composable(
            route = NavScreen.AddEditTaskScreen.route + "?taskId={taskId}&onEditTask={onEditTask}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("onEditTask") {
                    defaultValue = true
                    type = NavType.BoolType
                }
            )
        ){ backStackEntry ->

            val taskId = backStackEntry.arguments?.getString("taskId").toString()
            val onEditTask = backStackEntry.arguments?.getBoolean("onEditTask") == true

            if(taskId != "") addEditTaskViewModel.getTask(taskId.toInt())
            else addEditTaskViewModel.resetState()

            AddEditTaskScreen(
                navController = navigationController,
                addEditTaskViewModel = addEditTaskViewModel,
                taskID = taskId,
                onEditTask = mutableStateOf(onEditTask),
            )
        }
    }
}