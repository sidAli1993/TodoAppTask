package com.sid_ali_tech.todoapptask.domain.repository

import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.domain.model.RemoteTasks
import com.sid_ali_tech.todoapptask.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodosRepository {
    fun getTodos(): Flow<Response<RemoteTasks>>

    fun getTodosCompleted(): Flow<Response<RemoteTasks>>

    fun getTodosById(id:Int):Flow<Response<Todo>>

    fun saveTask(todo: Todo):Flow<Response<Todo>>

    fun updateTask(todo: Todo):Flow<Response<Todo>>


}