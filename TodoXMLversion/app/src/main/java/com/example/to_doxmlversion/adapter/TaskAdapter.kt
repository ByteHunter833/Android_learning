package com.example.to_doxmlversion.adapter

import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.to_doxmlversion.TaskDatabase
import com.example.to_doxmlversion.TaskEntity
import com.example.to_doxmlversion.databinding.TaskItemBinding

class TaskAdapter(
    private val tasks: MutableList<TaskEntity>,
    private val onDeleteTask: (Int) -> Unit,
    private val onEditTask: (Int) -> Unit,
    private val db: TaskDatabase,
    private val loadData: () -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.binding.tvTask.text = task.title

        holder.binding.checkBox.setOnCheckedChangeListener(null)
        holder.binding.checkBox.isChecked = task.isDone

        applyTaskStyle(holder, task)

        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                tasks[currentPosition].isDone = isChecked
                db.taskDao().updateTask(tasks[currentPosition])
                applyTaskStyle(holder, tasks[currentPosition])
                loadData()
            }
        }

        holder.binding.icDelete.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onDeleteTask(currentPosition)
            }
        }

        holder.binding.icEdit.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                onEditTask(currentPosition)
            }
        }
    }

    override fun getItemCount(): Int = tasks.size

    private fun applyTaskStyle(holder: TaskViewHolder, task: TaskEntity) {
        if (task.isDone) {
            holder.binding.tvTask.paintFlags =
                holder.binding.tvTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            holder.binding.tvTask.setTextColor(Color.GRAY)
        } else {
            holder.binding.tvTask.paintFlags =
                holder.binding.tvTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            holder.binding.tvTask.setTextColor(Color.BLACK)
        }
    }
}