package com.example.to_doxmlversion.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.to_doxmlversion.data.local.TaskEntity

@Dao
interface TaskDao {
    @Insert
    fun insertTask(task : TaskEntity)
    @Delete
    fun deleteTask(task : TaskEntity)
    @Update
    fun updateTask(task : TaskEntity)
    @Query("Select * from tasks")
    fun getAllTasks():List<TaskEntity>


    @Query("""
    SELECT * FROM tasks
    ORDER BY 
        CASE taskPriority
    WHEN 'Low' THEN 1
    WHEN 'Medium' THEN 2
    WHEN 'High' THEN 3
END
""")
    fun getTasksSortedByPriority(): List<TaskEntity>

}