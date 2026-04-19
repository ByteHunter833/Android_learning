package com.example.to_doxmlversion

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_doxmlversion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var binding: ActivityMainBinding
    private val tasks = mutableListOf<Task>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        taskAdapter = TaskAdapter(tasks)
        binding.recyclerview.adapter = taskAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(this)
        setContentView(binding.root)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val taskText = binding.textfield.text.trim().toString()
            if(taskText.isNotEmpty()){
                tasks.add(Task(title=taskText))
                binding.textfield.text.clear()
            }
        }
    }
}