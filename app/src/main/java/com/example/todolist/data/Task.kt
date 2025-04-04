package com.example.todolist.data

class Task(
    var id:Long,
    var title:String,
    var done:Boolean = false,//si esta hecha o no
    var category: Category
) {

    companion object{
        const val TABLE_NAME = "tasks"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DONE = "done"
        const val COLUMN_NAME_CATEGORY_ID = "category_id"
    }
}