package com.example.todolist.data

class Task(
    val id:Long,
    val title:String,
    val done:Boolean = false//si esta hecha o no
) {

    companion object{
        const val TABLE_NAME = "tasks"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DONE = "done"
    }
}