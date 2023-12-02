package com.sid_ali_tech.todoapptask.presentation.screens.addedittask

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sid_ali_tech.todoapptask.R
import com.sid_ali_tech.todoapptask.common.Constants
import com.sid_ali_tech.todoapptask.recivers.ReminderReceiver
import com.sid_ali_tech.todoapptask.ui.theme.DarkGray
import com.sid_ali_tech.todoapptask.utils.TopBar
import com.sid_ali_tech.todoapptask.ui.theme.interBold
import com.sid_ali_tech.todoapptask.ui.theme.MainBackground
import dev.jeziellago.compose.markdowntext.MarkdownText
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddEditTaskScreen(
    navController: NavController,
    addEditTaskViewModel: AddEditTaskViewModel,
    taskID: String,
    onEditTask: MutableState<Boolean> = mutableStateOf(true),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
) {
    val context = LocalContext.current
    var selectedDate=""
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MainBackground),
        scaffoldState = scaffoldState,
        backgroundColor = MainBackground,
        topBar = { TopBar(
            navigationController = navController,
            topBarTitle ="Add Task"
        )
        },
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = DarkGray,
                onClick = {
                if(onEditTask.value) {
                    addEditTaskViewModel.saveTask()
                    addEditTaskViewModel.resetState()
                    onEditTask.value = false
                    navController.popBackStack()
                }else{
                    onEditTask.value = true
                }
            }) {
                if(onEditTask.value) Icon(Icons.Filled.Check, null, tint = Color.White)
                else Icon(Icons.Filled.Edit, null, tint = Color.White)
            }
        }
    ) {

        val addEditTaskUiState = addEditTaskViewModel.addEditTaskUiState

        AddEditTaskContent(
            taskName = addEditTaskUiState.taskName,
            taskDescription = addEditTaskUiState.taskDescription,
            taskDueDate = addEditTaskUiState.taskDueDate,
            viewAsMarkdown = addEditTaskUiState.viewAsMarkdown,
            onTaskNameChange = addEditTaskViewModel::updateName,
            onTaskDescChange = addEditTaskViewModel::updateDescription,
            onTaskDueDateChange = addEditTaskViewModel::updateDueDate,
            onEditTask = true,
            onViewChange = addEditTaskViewModel::updateDescriptionView
        )
    }
}


@SuppressLint("UnrememberedMutableState")
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun AddEditTaskContent(
    taskName : String,
    taskDescription : String,
    taskDueDate : String,
    viewAsMarkdown : Boolean,
    onTaskNameChange: (String) -> Unit,
    onTaskDescChange : (String) -> Unit,
    onTaskDueDateChange: (String) -> Unit,
    onEditTask : Boolean,
    onViewChange : () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(top = 20.dp, start = 10.dp, end = 10.dp))
            .verticalScroll(rememberScrollState())
    ) {
        val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = DarkGray,
            disabledBorderColor = Color.Transparent,
            disabledTextColor = DarkGray,
            textColor = Color.Black,
            placeholderColor = Color.Gray
        )

        InputTaskName(
            taskName = taskName,
            onTaskNameChange = onTaskNameChange,
            textFieldColors = textFieldColors,
            onEditTask = onEditTask
        )

        InputTaskDueDate(
            taskDueDate = taskDueDate,
            onTaskDueDateChange = onTaskDueDateChange,
            onEditTask = onEditTask
        )

        if(viewAsMarkdown){
            MarkdownText(
                modifier = Modifier.padding(16.dp),
                markdown = taskDescription,
                fontSize = 16.sp,
                color = DarkGray,
                maxLines = 20,
                style = MaterialTheme.typography.overline,
            )
        }
        else{
            InputTaskDescription(
                taskDescription = taskDescription,
                onTaskDescChange = onTaskDescChange,
                textFieldColors = textFieldColors,
                onEditTask = onEditTask
            )
        }
    }

}

@Composable
fun InputTaskName(
    taskName : String,
    onTaskNameChange: (String) -> Unit,
    textFieldColors: TextFieldColors,
    onEditTask: Boolean
){
    OutlinedTextField(
        value = taskName,
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 70.dp),
        onValueChange = onTaskNameChange,
        placeholder = {
            Text(
                text = "Title",
                style = MaterialTheme.typography.h6,
                fontSize = 24.sp,
                fontFamily = interBold,
            )
        },
        textStyle = MaterialTheme.typography.h6.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            fontFamily = interBold
        ),
        maxLines = 1,
        colors = textFieldColors,
        enabled = onEditTask,
    )
}

@Composable
fun InputTaskDescription(
    taskDescription: String,
    onTaskDescChange: (String) -> Unit,
    textFieldColors: TextFieldColors,
    onEditTask: Boolean
){
    OutlinedTextField(
        value = taskDescription.replace("\\n", "\n"),
        onValueChange = onTaskDescChange,
        placeholder = { Text("Enter task description here.") },
        modifier = Modifier
            .height(350.dp)
            .fillMaxWidth(),
        colors = textFieldColors,
        enabled = onEditTask
    )
}


@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun InputTaskDueDate(
    taskDueDate: String,
    onTaskDueDateChange: (String) -> Unit,
    onEditTask: Boolean
) {
    val context = LocalContext.current

    val year : Int
    val month : Int
    val day : Int
    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year : Int, month : Int, dayOfMonth : Int ->

            var y = year.toString()
            var m = (month + 1).toString().padStart(2, '0')
            var d = dayOfMonth.toString().padStart(2, '0')
            
            onTaskDueDateChange("$y-$m-$d")

                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                val date = dateFormat.parse("$y-$m-$d")
                val dateInMillis = date.time
                setReminder(context,dateInMillis,"Alarm TODO","This is alarm task todo ",1)

        }, year, month, day
    )

    Row(modifier = Modifier
        .fillMaxWidth()
        .height(50.dp)
        .padding(PaddingValues(start = 1.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Text(text = if(taskDueDate != "No due date") "Due on $taskDueDate" else "No due date",
            color = Color.DarkGray,
            modifier = Modifier
                .padding(PaddingValues(start = 15.dp)))

        Image(
            modifier = Modifier
                .alpha(0.7f)
                .padding(PaddingValues(end = 15.dp))
                .clickable(
                    onClick = {
                        if (onEditTask) {
                            if (taskDueDate == "No due date") datePickerDialog.show()
                            else onTaskDueDateChange("No due date")
                        }
                    }),
            painter = painterResource(
                id = if (taskDueDate != "No due date" && onEditTask) R.drawable.close_icon
                     else R.drawable.calendar_icon
            ),
            contentDescription = null
        )
    }
}

@Composable
fun ViewAsMarkDown(
    viewAsMarkdown: Boolean,
    onViewChange: () -> Unit
){
    Row(modifier = Modifier.width(200.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Checkbox(
            checked = viewAsMarkdown,
            onCheckedChange = {
                onViewChange.invoke()
            },
            colors = CheckboxDefaults.colors(Color.Black)
        )

        Text(text = "View as markdown")
    }
}
fun setReminder(
    context: Context,
    date: Long,        // Date in milliseconds when the reminder should trigger
    title: String,     
    message: String,   
    priority: Int      
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, ReminderReceiver::class.java).apply {
        putExtra("TITLE", title)
        putExtra("MESSAGE", message)
        putExtra("PRIORITY", priority)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.set(AlarmManager.RTC_WAKEUP, date, pendingIntent)

    Toast.makeText(context, "Alarm/ Reminder has been set , ", Toast.LENGTH_SHORT).show()
}

