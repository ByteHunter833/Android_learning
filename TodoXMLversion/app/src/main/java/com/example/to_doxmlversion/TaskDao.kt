package com.example.to_doxmlversion

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Insert
    fun inserTask(task : TaskEntity)
    @Delete
    fun deleteTask(task : TaskEntity)
    @Update
    fun updateTask(task : TaskEntity)
    @Query("Select * from tasks")
    fun getAllTasks():List<TaskEntity>
}