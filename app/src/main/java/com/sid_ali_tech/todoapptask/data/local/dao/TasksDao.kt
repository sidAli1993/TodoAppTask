package com.sid_ali_tech.todoapptask.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sid_ali_tech.todoapptask.domain.model.Todo

@Dao
interface TasksDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveData(todo: Todo):Long

    @Query("SELECT * FROM TodoTasks WHERE completed=:completed ORDER BY id DESC")
    fun getAllTasks(completed: Boolean=false):List<Todo>

    @Query("SELECT * FROM TodoTasks WHERE completed=:completed")
    fun getCompletedTasks(completed:Boolean=true):List<Todo>

    @Query("SELECT * FROM TodoTasks WHERE id=:id")
    fun getTaskById(id:Int):Todo

    @Query("UPDATE TODOTASKS SET completed=:completed WHERE id=:id")
    fun updateData(id:Int,completed: Boolean=true)

    @Delete
    fun deleteCreation(todo: Todo):Int


}