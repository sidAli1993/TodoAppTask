package com.sid_ali_tech.todoapptask.presentation.screens.addedittask

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid_ali_tech.todoapptask.common.Constants.TAG
import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.domain.model.Todo
import com.sid_ali_tech.todoapptask.domain.usecases.GetTaskByIdUseCase
import com.sid_ali_tech.todoapptask.domain.usecases.SaveTodoUseCase
import com.sid_ali_tech.todoapptask.domain.usecases.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditTaskUiState(
    var taskName: String = "",
    val taskDescription: String = "",
    val taskDueDate: String = "No due date",
    val isTaskFinished: Boolean = false,
    val taskId: String = "",
    val isLoading: Boolean = false,
    val isTaskSaved: Boolean = false,
    val onEditTask: Boolean = false,
    val viewAsMarkdown: Boolean = false
)

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTodoUseCase: SaveTodoUseCase,
    private val updateTodoUseCase: UpdateTodoUseCase
) : ViewModel() {


    var addEditTaskUiState by mutableStateOf(AddEditTaskUiState())
        private set

    fun updateName(newName: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskName = newName)
    }

    fun updateDescription(newDescription: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskDescription = newDescription)
    }

    fun updateDueDate(newDate: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskDueDate = newDate)
    }

    fun updateTaskId(taskId: String) {
        addEditTaskUiState = addEditTaskUiState.copy(taskId = taskId)
    }

    fun updateDescriptionView() {
        addEditTaskUiState =
            addEditTaskUiState.copy(viewAsMarkdown = !addEditTaskUiState.viewAsMarkdown)
    }

    fun saveTask() {
        viewModelScope.launch {
            saveTodoUseCase.invoke(Todo(
                Category = addEditTaskUiState.taskDescription,
                Title = addEditTaskUiState.taskName,
                completed = false,
                date = addEditTaskUiState.taskDueDate,
            )).collect(){
                Log.e(TAG, "saveTask: $it ", )
            }
        }
    }

    fun getTask(id: Int) {
        viewModelScope.launch {
            getTaskByIdUseCase.invoke(id).collect() { result ->
                when (result) {
                    is Response.Loading -> {}

                    is Response.Success -> {
                        val todo = result.data
                        todo?.let {
                            addEditTaskUiState =
                                todo.Title.let { it1 -> addEditTaskUiState.copy(taskName = it1) }!!
                            addEditTaskUiState =
                                it.Category.let { it1 -> addEditTaskUiState.copy(taskDescription = it1) }!!
                            addEditTaskUiState =
                                it.date.let { it1 -> addEditTaskUiState.copy(taskDueDate = it1) }!!
                            addEditTaskUiState =
                                it.id.let { it1 -> addEditTaskUiState.copy(taskId = it1.toString()) }!!
                            addEditTaskUiState =
                                it.completed?.let { it1 -> addEditTaskUiState.copy(isTaskFinished = it1) }!!
                        }


                    }

                    is Response.Error -> {}
                    else->{}
                }
            }
        }
    }

    fun resetState() {
        addEditTaskUiState = AddEditTaskUiState()
    }

}