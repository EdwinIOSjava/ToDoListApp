package com.example.todolist.managers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.todolist.data.Category
import com.example.todolist.data.Task

class DatabaseManager(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        const val DATABASE_NAME = "ToDoList.db"
        const val DATABASE_VERSION = 2



        // creamos la tabla de las Task
        private const val SQL_CREATE_TABLE_TASK =
            "CREATE TABLE ${Task.TABLE_NAME} (" +
                    "${Task.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Task.COLUMN_NAME_TITLE} TEXT," +
                    "${Task.COLUMN_NAME_DONE} BOOLEAN," +
                    "${Task.COLUMN_NAME_CATEGORY_ID} INTEGER,"+
                    "FOREIGN KEY(${Task.COLUMN_NAME_CATEGORY_ID}) " +
                    "REFERENCES ${Category.TABLE_NAME}(${Category.COLUMN_NAME_ID}) ON DELETE CASCADE)"

        //nombre de la columna , tipo de dato :
        //${Task.COLUM_NAME_ID} Int

        private const val SQL_DROP_TABLE_TASK = "DROP TABLE IF EXISTS ${Task.TABLE_NAME}"

        // creamos la tabla de las Categories

        private const val SQL_CREATE_TABLE_CATEGORY =
            "CREATE TABLE ${Category.TABLE_NAME} (" +
                    "${Category.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${Category.COLUMN_NAME_TITLE} TEXT)"


        private const val SQL_DROP_TABLE_CATEGORY = "DROP TABLE IF EXISTS ${Category.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {//cuando se cree la base de datos aprovechamos para crear las tablas, en este caso solo 1
        db.execSQL(SQL_CREATE_TABLE_CATEGORY)// primerop la TABLA CATEGORIA porque  en la de TASK la llamamos y asi o nos da error
        db.execSQL(SQL_CREATE_TABLE_TASK)//se crea la tabla
        Log.i("DATABASE", "Created table Tasks and Categories")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    fun onDestroy(db: SQLiteDatabase) {
        db.execSQL(SQL_DROP_TABLE_TASK)
        db.execSQL(SQL_DROP_TABLE_CATEGORY)
    }
}