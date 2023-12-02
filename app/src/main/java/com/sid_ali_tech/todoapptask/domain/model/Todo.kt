package com.sid_ali_tech.todoapptask.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TodoTasks")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val Category: String="",
    val Title: String="",
    var completed: Boolean=false,
    val date: String="",
    val priority: String="High",
    val todo: String="",
    val userId: Int=0
)