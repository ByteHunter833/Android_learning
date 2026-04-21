package com.example.to_doxmlversion

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doxmlversion.adapter.TaskAdapter
import com.example.to_doxmlversion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TaskDatabase

    private val tasks = mutableListOf<TaskEntity>()
    private var editingPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TaskDatabase.getDatabase(this)
        val priorityAdapter  = ArrayAdapter.createFromResource(
            this,
            R.array.priority_array,
            android.R.layout.simple_spinner_item
        )
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPriority.adapter = priorityAdapter
        setupRecyclerView()
        setupInsets()
        setupListeners()

        loadData()
    }

    private fun setupRecyclerView() {
        taskAdapter = TaskAdapter(
            tasks,
            onDeleteTask = { position ->
                AlertDialog.Builder(this)
                    .setTitle("Delete Task")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Yes") { _, _ ->
                        db.taskDao().deleteTask(tasks[position])
                        loadData()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            },
            onEditTask = { position ->
                if (tasks[position].isDone) {
                    Toast.makeText(this, "You can't edit a done task", Toast.LENGTH_SHORT).show()
                } else {
                    binding.textfield.setText(tasks[position].title)
                    editingPosition = position
                    binding.button.text = getString(R.string.update_the_task)
                }
            },
            db = db,
            loadData = { loadData() }
        )

        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        binding.recyclerview.adapter = taskAdapter
    }

    private fun setupListeners() {
        binding.button.setOnClickListener {
            val taskText = binding.textfield.text.toString().trim()
            val selectedPriority = binding.spinnerPriority.selectedItem.toString()
            Log.d("Debug", "Selected Priority: $selectedPriority")
            if (taskText.isEmpty()) {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()

            }


            if (editingPosition != -1) {
                tasks[editingPosition].title = taskText
                db.taskDao().updateTask(tasks[editingPosition])
                loadData()
                editingPosition = -1
                binding.button.text = getString(R.string.add_task)
            } else {
                db.taskDao().insertTask(TaskEntity(title = taskText, taskPriority = selectedPriority))
                loadData()
            }

            binding.textfield.text.clear()
        }
    }

    private fun setupInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadData() {
        tasks.clear()
        tasks.addAll(db.taskDao().getAllTasks())
        taskAdapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        if (tasks.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.recyclerview.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.recyclerview.visibility = View.VISIBLE
        }
    }
}