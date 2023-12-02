package com.sid_ali_tech.todoapptask.data.repository

import android.util.Log
import com.sid_ali_tech.todoapptask.common.Constants.AppCycle
import com.sid_ali_tech.todoapptask.common.Constants.TAG
import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.data.local.AppDatabase
import com.sid_ali_tech.todoapptask.domain.model.Todo
import com.sid_ali_tech.todoapptask.data.remote.ApiService
import com.sid_ali_tech.todoapptask.domain.model.RemoteTasks
import com.sid_ali_tech.todoapptask.domain.repository.TodosRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import java.lang.Exception
import javax.inject.Inject

class TodosRepositoryImpl @Inject constructor(private val apiService: ApiService,private val appDatabase: AppDatabase): TodosRepository {
    override fun getTodos(): Flow<Response<RemoteTasks>> = channelFlow{
        try {
            trySend(Response.Loading)
            if (AppCycle){
                val resp = apiService.getTodos()
                resp.todos.forEach {
                    appDatabase.taskDao()?.saveData(it)
                }
                delay(1000)
                AppCycle=false
            }
            if (!appDatabase.taskDao()?.getAllTasks(completed = false).isNullOrEmpty()){
                trySend(Response.Success(RemoteTasks(appDatabase.taskDao()?.getAllTasks()!!)))
            }else{
                trySend(Response.Error("tasks are null"))
            }
        } catch (e: Exception) {
            trySend(Response.Error("unexpected error occoured ${e.message}"))
        }
        awaitClose()
    }

    override fun getTodosCompleted(): Flow<Response<RemoteTasks>> = channelFlow{
        try {
            trySend(Response.Loading)
            if (!appDatabase.taskDao()?.getCompletedTasks(completed = true).isNullOrEmpty()){
                trySend(Response.Success(RemoteTasks(appDatabase.taskDao()?.getCompletedTasks()!!)))
            }else{
                trySend(Response.Error("tasks are null"))
            }
        } catch (e: Exception) {
            trySend(Response.Error("unexpected error occoured ${e.message}"))
        }
        awaitClose()
    }

    override fun getTodosById(id: Int): Flow<Response<Todo>> = channelFlow{
        try {
            trySend(Response.Loading)

            val todo=appDatabase.taskDao()?.getTaskById(id)

            trySend(Response.Success(todo))
        } catch (e: Exception) {
            trySend(Response.Error("unexpected error occoured ${e.message}"))
        }
        awaitClose()
    }

    override fun saveTask(todo: Todo): Flow<Response<Todo>> = channelFlow {
        try {
            trySend(Response.Loading)

           val i= appDatabase.taskDao()?.saveData(todo = todo)
            Log.e(TAG, "saveTask: $i", )
            trySend(Response.Success(todo))
        } catch (e: Exception) {
            trySend(Response.Error("unexpected error occoured ${e.message}"))
        }
        awaitClose()
    }

    override fun updateTask(todo: Todo): Flow<Response<Todo>> = channelFlow{
        try {
            trySend(Response.Loading)

//            appDatabase.taskDao()?.updateData(todo = todo)

            trySend(Response.Success(todo))
        } catch (e: Exception) {
            trySend(Response.Error("unexpected error occoured ${e.message}"))
        }
        awaitClose()
    }


}