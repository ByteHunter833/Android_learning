package com.example.to_doxmlversion

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doxmlversion.adapter.TaskAdapter
import com.example.to_doxmlversion.databinding.ActivityMainBinding
import com.example.to_doxmlversion.model.Task

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding
    private val tasks = mutableListOf<Task>()
    private var editingPostion : Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskAdapter = TaskAdapter(tasks, onDeleteTask = { position ->
            tasks.removeAt(position)
            taskAdapter.notifyItemRemoved(position)
        }, onEditTask = { position -> binding.textfield.setText(tasks[position].title); editingPostion = position; binding.button.text =
            "Update the task" })

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = taskAdapter

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val taskText = binding.textfield.text.toString().trim()

            if (taskText.isNotEmpty()) {
                if(editingPostion!=-1){
                    tasks[editingPostion].title = taskText
                    taskAdapter.notifyItemChanged(editingPostion)
                    editingPostion = -1
                    binding.textfield.text.clear()
                    binding.button.text = "Add Task"
                }else{
                    tasks.add(Task(title = taskText))
                    taskAdapter.notifyItemInserted(tasks.size - 1)
                    binding.textfield.text.clear()
                }

            }else{
                Toast.makeText(
                    this, "Please enter a task ", Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}