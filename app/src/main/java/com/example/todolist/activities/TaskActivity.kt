package com.example.todolist.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.todolist.R
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityTaskBinding

class TaskActivity : AppCompatActivity() {
    companion object {
        const val TASK_ID = "TASK_ID"
    }

    lateinit var binding: ActivityTaskBinding
    lateinit var taskDAO: TaskDAO

    // creamos task de tipo Task para poder acceder a sus propiedades
    lateinit var task: Task

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
        val taskId = intent.getLongExtra(
            TASK_ID,
            -1
        )// asignamos -1 porque  si no tiene nada el id es -1 entonces seria una tarea nueva,
        // si lo tiene es una tarea existente

        taskDAO = TaskDAO(this)
// hacmeos un if para saber si la tarea es nueva o si es una tarea existente
        if (taskId != -1L)// significa que es una tarea existente
        {
            // se crea una tarea nueva con el valor de la tarea existente buscando el ID en la base de datos
            task = taskDAO.findById(taskId)!!
            binding.titleEditText.setText(task.title)// ese valor que ya tenia se pone por defecto en el editText y asi la podremos editar y guardar.
        } else {
            task = Task(
                -1L,
                ""
            )// significa que es una tarea nueva entonces creamos una nueva tarea con un id de -1 y un titulo vacio
        }

        binding.saveButton.setOnClickListener {
            val title =
                binding.titleEditText.text.toString()// obtenemos el titulo que se ha escrito en el editText
            task = Task(
                task.id,
                title
            )// creamos una nueva tarea con el titulo que se ha escrito en el editText


            // es aqui donde usamos el -1 .... si es -1 significa que es una tarea nueva y si no es -1 significa que es una tarea existente
            // si es -1 lo guardamos en la base de datos y si no lo actualizamos
            if (task.id != -1L) {
                taskDAO.update(task)
            } else {
                taskDAO.insert(task)
            }
            finish()//cierra la actividad y nos devuelve a la actividad anterior
        }
    }
}