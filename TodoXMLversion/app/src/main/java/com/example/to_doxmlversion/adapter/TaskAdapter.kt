package com.example.to_doxmlversion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_doxmlversion.databinding.TaskItemBinding
import com.example.to_doxmlversion.model.Task

class TaskAdapter (private val tasks : List<Task>, private  val onDeleteTask : (Int)-> Unit, private  val onEditTask : (Int)-> Unit) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
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
        holder.binding.checkBox.isChecked = task.isDone
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked -> task.isDone = isChecked }
        holder.binding.icDelete.setOnClickListener { onDeleteTask(position) }
        holder.binding.icEdit.setOnClickListener { onEditTask(position ) }
    }
    override fun getItemCount(): Int {
        return tasks.size
    }

}