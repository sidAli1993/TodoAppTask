package com.sid_ali_tech.todoapptask.presentation.task

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sid_ali_tech.todoapptask.R
import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.data.local.AppDatabase
import com.sid_ali_tech.todoapptask.domain.model.Todo
import com.sid_ali_tech.todoapptask.ui.theme.DarkGray
import com.sid_ali_tech.todoapptask.ui.theme.interLight
import com.sid_ali_tech.todoapptask.ui.theme.interMedium
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@SuppressLint("UnrememberedMutableState")
@Composable
fun TaskList(
    currDateTasks : String = "",
    displayFinishedTask : Boolean = false,
    navController: NavController,
    taskListViewModel: TaskListViewModel = hiltViewModel(),
){
    if (displayFinishedTask){
        var tasksList:List<Todo> ?= null
        val taskState by taskListViewModel.allTasksCompleted.collectAsState()

        DisposableEffect(Unit) {
            taskListViewModel.getAllTasksCompleted()
            onDispose { /* Dispose logic if needed */ }
        }
        when (taskState) {
            is Response.Loading -> {
            }
            is Response.Success -> {
                tasksList=(taskState as Response.Success).data?.todos
                if (!tasksList.isNullOrEmpty()){
                    taskListViewModel.totalNumberOfTasksRemaining.value=tasksList.size
                    SetData(todos = tasksList, taskListViewModel = taskListViewModel, navController = navController)
                }
            }
            is Response.Error -> {
                val errorMessage = (taskState as Response.Error).message
                Text(text = errorMessage, color = Color.Red)
            }
        }
    }else{
        var tasksList:List<Todo> ?= null
        val taskState by taskListViewModel.allTasks.collectAsState()

        DisposableEffect(Unit) {
            taskListViewModel.getAllTasks()
            onDispose { /* Dispose logic if needed */ }
        }
        when (taskState) {
            is Response.Loading -> {
            }
            is Response.Success -> {
                tasksList=(taskState as Response.Success).data?.todos
                if (!tasksList.isNullOrEmpty()){
                    taskListViewModel.totalNumberOfTasksRemaining.value=tasksList.size
                    SetData(todos = tasksList, taskListViewModel = taskListViewModel, navController = navController)
                }
            }
            is Response.Error -> {
                val errorMessage = (taskState as Response.Error).message
                Text(text = errorMessage, color = Color.Red)
            }
        }
    }


}

@Composable
fun SetData(todos:List<Todo>,taskListViewModel: TaskListViewModel, currDateTasks : String = "",
            displayFinishedTask : Boolean = false,
            navController: NavController,) {
    LazyColumn(
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)){

        itemsIndexed(
            items =todos,
            itemContent = {_, item->
                AnimatedVisibility(
                    visible = !taskListViewModel.deletedTasks.contains(item.id.toString()),
                    enter = slideInHorizontally() + fadeIn(),
                    exit = slideOutHorizontally() + fadeOut()
                ) {
                    if(item.id.toString() != ""){
                        TaskCard(
                            task = item,
                            navController = navController,
                            onDeleteTask = taskListViewModel::deleteTask,
                            onFinishTask = taskListViewModel::finishTask
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun TaskCard(
    task : Todo,
    navController: NavController,
    onDeleteTask: (String) -> Unit,
    onFinishTask: (String, Boolean) -> Unit
){
    val context= LocalContext.current
    val appDatabase=AppDatabase(context)

    val params = ("?taskId=${task.id}")

    val editTask = SwipeAction(
        onSwipe = {
            appDatabase.taskDao()?.updateData(task.id,true)
            onDeleteTask(task.id.toString())
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.splash_check_icon),
                contentDescription = null)
        },
        background = Color(0XFF4CAF50)
    )

    val deleteTask = SwipeAction(
        onSwipe = {
            val i=appDatabase.taskDao()?.deleteCreation(task)
            Toast.makeText(context, "deleted success  $i", Toast.LENGTH_SHORT).show()
            onDeleteTask(task.id.toString())
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.delete_icon),
                contentDescription = null)
        },
        background = Color(0XFFFF9595)
    )
    SwipeableActionsBox(
        modifier = Modifier
            .shadow(10.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Transparent)
            .clickable(onClick = {
            }),
        swipeThreshold = 100.dp,
        startActions = listOf(editTask),
        endActions = listOf(deleteTask)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(75.dp)
                .background(Color.White)
                .padding(PaddingValues(start = 20.dp, end = 10.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {

            val taskId = task.id
            val isFinished = task.completed

            if (isFinished != null) {
                Checkbox(
                    checked = isFinished,
                    onCheckedChange = {
                        if (taskId != null)
                            onFinishTask(taskId.toString(), it)
                    },
                    colors = CheckboxDefaults.colors(Color.Black)
                )
            }

            Column(
                modifier = Modifier.padding(PaddingValues(start = 20.dp)),
                horizontalAlignment = Alignment.Start
            ) {

                task.Title?.let {
                    Text(
                        text = it,
                        fontFamily = interMedium,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = DarkGray,
                        textDecoration =
                            if(task.completed == true) TextDecoration.LineThrough
                            else TextDecoration.None
                    )
                }

                task.date.let {
                    Text(
                        text = if (it != "No due date") "Due on $it" else "No due date",
                        fontFamily = interLight,
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = DarkGray
                    )
                }
            }
        }
    }
}