package com.example.todolist

class Task(
    val id:Long,
    val title:String,
    val done:Boolean,//si esta hecha o no
) {
    companion object{
        const val TABLE_NAME = "tasks"

        const val COLUM_NAME_ID = "id"
        const val COLUM_NAME_TITLE = "title"
        const val COLUM_NAME_DONE = "done"
    }
}