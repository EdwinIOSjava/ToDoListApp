package com.example.todolist.data

data class CategoryWithTasks(
    val id: Long,
    val title: String,
    val tasks: List<Task>

)
