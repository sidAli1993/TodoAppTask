package com.sid_ali_tech.todoapptask.domain.usecases

import com.sid_ali_tech.todoapptask.common.Response
import com.sid_ali_tech.todoapptask.domain.model.RemoteTasks
import com.sid_ali_tech.todoapptask.domain.model.Todo
import com.sid_ali_tech.todoapptask.domain.repository.TodosRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SaveTodoUseCase @Inject constructor(private val todosRepository: TodosRepository){
    operator fun invoke(todo: Todo):Flow<Response<Todo>>{
        return todosRepository.saveTask(todo )
    }
}