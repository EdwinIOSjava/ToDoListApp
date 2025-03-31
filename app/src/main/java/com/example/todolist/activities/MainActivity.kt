package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.TaskAdapter
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var taskDAO: TaskDAO
    lateinit var taskList: List<Task>

    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        taskDAO = TaskDAO(this)
        // asignamos al adapter la lista de tareas vacia y una funcion lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento
        // en el recycler view

// usamos las func Lambda para que al darle click en un texto , podamos modificar el texto de la tarea.
        adapter = TaskAdapter(emptyList(), { position ->
            // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion
            val task = taskList[position]

            //
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(TaskActivity.TASK_ID, task.id)
            startActivity(intent)
        },{position ->// AQUI VA LA SEGUNDA FUNC LAMBDA que usaremos para Borrar la tarea al presionar el boton de borrar

            val task = taskList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

            taskDAO.delete(task) // usamos la funcion delete de la clase TaskDAO para borrar la tarea de la base de datos

            refreshData() // por ultimo llamamos a la funcion que refresca los datos del recycler view

        })
        // asignamos el adapter al recycler view
        binding.recyclerView.adapter = adapter
        // asignamos el layout manager al recycler view
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // asignamos el listener al boton de añadir tarea
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()

        refreshData()

    }

    fun refreshData(){
        taskList = taskDAO.findAll()// usamos la funcion findAll de la clase TaskDAO para obtener todas las tareas de la base de datos
        adapter.updateItems(taskList)// usamos la funcion updateItems de la clase TaskAdapter para actualizar los datos del recycler view
    }
}