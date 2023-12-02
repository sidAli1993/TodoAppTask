package com.sid_ali_tech.todoapptask.presentation.task

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.domain.model.RemoteTasks
import com.sid_ali_tech.todoapptask.domain.usecases.GetTodoListCompletedUseCase
import com.sid_ali_tech.todoapptask.domain.usecases.GetTodoListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTodoListUseCase: GetTodoListUseCase,
    private val getTodoListCompletedUseCase: GetTodoListCompletedUseCase
) : ViewModel() {

    private var _allTasks = MutableStateFlow<Response<RemoteTasks>>(
        Response.Success(
            RemoteTasks()
        )
    )
    val allTasks: StateFlow<Response<RemoteTasks>> = _allTasks


    private var _allTasksCompleted = MutableStateFlow<Response<RemoteTasks>>(
        Response.Success(
            RemoteTasks()
        )
    )
    val allTasksCompleted: StateFlow<Response<RemoteTasks>> = _allTasksCompleted


    var totalNumberOfTasksRemaining=MutableLiveData<Int>(0)
    val numOfTasks:LiveData<Int> get() = totalNumberOfTasksRemaining

    fun getAllTasks(){
        viewModelScope.launch {
            getTodoListUseCase.invoke().collect(){
                _allTasks.value=it
            }
        }
    }

    fun getAllTasksCompleted(){
        viewModelScope.launch {
            getTodoListCompletedUseCase.invoke().collect(){
                _allTasksCompleted.value=it
            }
        }
    }

    fun clearAllCreations(){
        _allTasks.value= Response.Success(RemoteTasks())
    }

    init {
        updateTaskList()
    }

    private fun updateTaskList() {
    }

    val deletedTasks = mutableStateListOf<String>()

    fun deleteTask(taskId : String){
        deletedTasks.add(taskId)
    }

    fun finishTask(taskId : String, isFinished : Boolean) = viewModelScope.launch {
    }
}