package com.example.todolist.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist.R
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {
    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
        const val TASK_ID = "TASK_ID"

    }

    lateinit var binding: ActivityTaskBinding
    lateinit var taskDAO: TaskDAO

    // creamos task de tipo Task para poder acceder a sus propiedades
    lateinit var task: Task
    lateinit var categoryDAO: CategoryDAO
    lateinit var category: Category

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //recuperamos el id de la tarea que se ha pulsado en el recycler view
        //RECIBIMOS EL EXTRA DEL MAIN ACTIVITY

        val id = intent.getLongExtra(TASK_ID, -1L)
        val categoryId = intent.getLongExtra(
            CATEGORY_ID, -1L
        )// asignamos -1 porque  si no tiene nada el id es -1 entonces
        // seria una tarea nueva, si lo tiene es una tarea existente

        taskDAO = TaskDAO(this)
        categoryDAO = CategoryDAO(this)
        category = categoryDAO.findById(categoryId)!!
// hacmeos un if para saber si la tarea es nueva o si es una tarea existente
        if (id != -1L)// significa que es una tarea existente
        {
            // se crea una tarea nueva con el valor de la tarea existente buscando el ID en la base de datos
            task = taskDAO.findById(id)!!
            binding.titleEditText.setText(task.title)// ese valor que ya tenia se pone por defecto en el editText y asi la podremos editar y guardar.
            supportActionBar?.title = "Editar tarea"
        } else {
            task = Task(-1L, "", false, category)
            supportActionBar?.title = "Crear tarea"
        }

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()

            task.title = title

            if (task.id != -1L) {
                taskDAO.update(task)
            } else {
                taskDAO.insert(task)
            }

            finish()

        }
    }
}