package com.sid_ali_tech.todoapptask.data.remote

import com.sid_ali_tech.todoapptask.domain.model.RemoteTasks
import com.sid_ali_tech.todoapptask.domain.model.Todo
import retrofit2.http.GET

interface ApiService {
    @GET("v1/970ec59d-1762-492b-90c0-2e60fa2d1bb4")
    suspend fun getTodos(): RemoteTasks
}