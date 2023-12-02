package com.sid_ali_tech.todoapptask.presentation.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sid_ali_tech.todoapptask.common.Constants.TAG
import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.domain.model.Todo
import com.sid_ali_tech.todoapptask.presentation.navigations.NavScreen
import com.sid_ali_tech.todoapptask.utils.TopBar
import com.sid_ali_tech.todoapptask.presentation.task.TaskList
import com.sid_ali_tech.todoapptask.presentation.task.TaskListViewModel
import com.sid_ali_tech.todoapptask.ui.theme.DarkGray
import com.sid_ali_tech.todoapptask.ui.theme.MainBackground
import com.sid_ali_tech.todoapptask.utils.NoTaskDisplay
import com.sid_ali_tech.todoapptask.ui.theme.interBold
import com.sid_ali_tech.todoapptask.ui.theme.interLight
import kotlinx.coroutines.cancel
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController,
    openDrawer:() -> Unit,
    scaffoldState: ScaffoldState,
    homeViewModel: HomeViewModel,
    taskListViewModel: TaskListViewModel = hiltViewModel()
){
    val numberOfTasks by taskListViewModel.numOfTasks.observeAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = { TopBar(navigationController = navController, openDrawer = openDrawer, topBarTitle = "HOME") },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = DarkGray,
                onClick = {
                    navController.navigate(NavScreen.AddEditTaskScreen.route)
                }) {
                Icon(Icons.Filled.Add, null, tint = Color.White)
            }
        },
        backgroundColor = MainBackground,
//        drawerContent = { SideDrawer(navController = navController) }
    ) {

        val homeUiState = homeViewModel.homeUiState
        val state = rememberCollapsingToolbarScaffoldState()
        CollapsingToolbarScaffold(
            modifier = Modifier
                .fillMaxSize(),
            state = state,
            scrollStrategy = ScrollStrategy.EnterAlways,
            toolbar = {
                val size =  (state.toolbarState.progress).dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MainBackground)
                        .parallax(0.6f)
                        .graphicsLayer {
                            alpha = state.toolbarState.progress
                        }
                        .padding(PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(PaddingValues(top = 20.dp, bottom = 20.dp)),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {

                        Column() {
//                            UserName()
                            Spacer(modifier = Modifier.height(10.dp))
                            CurrentNumberOfTasks(numberOfTasks!!)
                        }

//                        UserPicture(70)
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                    )
                }

                Spacer(
                    modifier = Modifier
                        .road(Alignment.CenterStart, Alignment.BottomEnd)
                        .padding(60.dp, 16.dp, 16.dp, 16.dp)
                        .size(size),
                )
            }
        ){

            Box(modifier = Modifier
                .fillMaxSize()){

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp))
                ) {

                    TaskTabButtons(
                        onActiveButtonChange = homeViewModel::onActiveButtonChange,
                        homeUiState = homeUiState
                    )

                    if (homeUiState.activeButton == "todo") {
                        TaskList(
                            displayFinishedTask = false,
                            navController = navController,
                        )
                    } else {
                        TaskList(
                            displayFinishedTask = true,
                            navController = navController,
                        )
                    }

                }

                Spacer(
                    Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MainBackground
                                )
                            )
                        )
                        .align(Alignment.BottomCenter)
                )

            }

        }
    }
}

@Composable
fun TaskTabButtons(
    onActiveButtonChange: (String) -> Unit,
    homeUiState: HomeUiState
) {

    val interactionSource = remember { MutableInteractionSource() }

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(PaddingValues(start = 10.dp, end = 10.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {

        Box(modifier = Modifier
            .height(45.dp)
            .width(150.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onActiveButtonChange("todo")
                }),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "TO DO",
                fontFamily = interBold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (homeUiState.activeButton == "todo") Color.Black else Color(0XFFC6C6C6)
            )
        }

        Divider(modifier = Modifier
            .height(20.dp)
            .width(1.dp)
            .background(Color(0XFFABABAB)))

        Box(modifier = Modifier
            .height(45.dp)
            .width(150.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    onActiveButtonChange("done")
                }),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "DONE",
                fontFamily = interBold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (homeUiState.activeButton == "done") Color.Black else Color(0XFFC6C6C6)
            )
        }
    }

    Divider(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color(0XFFABABAB)))
}

@Composable
fun CurrentNumberOfTasks(
    numberOfTasks:Int=0
){
    Row(modifier = Modifier.width(150.dp)) {
        Text(
            text =
            if(numberOfTasks>0) {
                "You currently have ${numberOfTasks  } unfinished tasks"
            }
            else{
                "Yay!, You currently don't have any task"
            },
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            fontFamily = interLight
        )
    }
}

@Composable
fun InfoWithIcon(info : String, icon : Painter){
    Row(
        modifier = Modifier.height(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(14.dp),
            painter = icon,
            contentDescription = null
        )
        Text(
            text = info,
            fontWeight = FontWeight.Light,
            fontFamily = interLight,
            fontSize = 12.sp
        )
    }
}

@Preview
@Composable
fun ShowPreview(){

}