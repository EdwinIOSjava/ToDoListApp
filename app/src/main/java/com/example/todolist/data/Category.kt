package com.example.todolist.data

data class Category(
    val id: Long,
    var title: String,
//    var color: Int,
//    var icon: Int
) {
    companion object{
        const val TABLE_NAME = "Categories"
        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
//        const val COLUMN_NAME_COLOR = "color"
//        const val COLUMN_NAME_ICON = "icon"

    }
}