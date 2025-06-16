package com.angelbustamante.tasklist.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.angelbustamante.tasklist.data.Task

class TaskViewModel : ViewModel() {
    private val _tasks = mutableStateListOf<Task>()
    val tasks: List<Task> get() = _tasks
    private var nextId = 1

    fun addTask(title: String, dueDate: String) {
        _tasks.add(Task(nextId++, title, dueDate))
    }

    fun updateTask(id: Int, title: String, dueDate: String) {
        _tasks.replaceAll { if (it.id == id) it.copy(title = title, dueDate = dueDate) else it }
    }

    fun deleteTask(id: Int) {
        _tasks.removeIf { it.id == id }
    }
}