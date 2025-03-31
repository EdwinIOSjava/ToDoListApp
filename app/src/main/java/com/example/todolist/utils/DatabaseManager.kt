package com.example.todolist.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.todolist.data.Task

class DatabaseManager (context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
){
    companion object {
        const val DATABASE_NAME = "ToDoList.db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_TABLE_TASK =
            "CREATE TABLE ${Task.TABLE_NAME} (" +
                    "${Task.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Task.COLUMN_NAME_TITLE} TEXT," +
                    "${Task.COLUMN_NAME_DONE} BOOLEAN)"
        //nombre de la columna , tipo de dato :
        //${Task.COLUM_NAME_ID},Int

    private const val SQL_DROP_TABLE_TASK = "DROP TABLE IF EXISTS ${Task.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {//cuando se cree la base de datos aprovechamos para crear las tablas, en este caso solo 1
        db.execSQL(SQL_CREATE_TABLE_TASK)//se crea la tabla
        Log.i("DATABASE", "Created table Tasks")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int ) {
        onDestroy(db)
        onCreate(db)
    }
    fun onDestroy(db: SQLiteDatabase){
        db.execSQL(SQL_DROP_TABLE_TASK)
    }
}