package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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
        adapter = TaskAdapter(
            emptyList(),
            { position -> // esta es la primera funcion Lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento en el recycler view
                // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion
                val task = taskList[position]

                val intent = Intent(this, TaskActivity::class.java)
                intent.putExtra(TaskActivity.TASK_ID, task.id)// aqui le pasamos el id de la tarea que se ha pulsado en el recycler view a la actividad TaskActivity para que pueda ser editada
                startActivity(intent)
            },
                ::deleteTaskFuntionLambda, // esta es la seguda funcion Lambda pero la hemos  bautizado y creado a lo ultimo para ejemplificar que se puede hacer asi
                ::doneCheckBoxFuntionLambda //  tercera funcion Lambda que se ejecuta cuando se hace click en el checkbox
            )
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

    private fun refreshData() {
        taskList =
            taskDAO.findAll()// usamos la funcion findAll de la clase TaskDAO para obtener todas las tareas de la base de datos
        adapter.updateItems(taskList)// usamos la funcion updateItems de la clase TaskAdapter para actualizar los datos del recycler view
    }

    fun deleteTaskFuntionLambda(position: Int){
        // AQUI VA LA SEGUNDA FUNC LAMBDA que usaremos para Borrar la tarea al presionar el boton de borrar

        val task =taskList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

        AlertDialog.Builder(this)
            .setTitle("Delete task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { _, _ ->
                taskDAO.delete(task) // usamos la funcion delete de la clase TaskDAO para borrar la tarea de la base de datos
                refreshData() // por ultimo llamamos a la funcion que refresca los datos del recycler view
            }
            .setNegativeButton(android.R.string.cancel, null)// no haremos nada-- usamos R.string.cancel porque ya existe en el android  y el mismo lo traduce
            .setCancelable(false)// esto es para que si pulsamos fuera del dialogo no se cierre
            .show()
    }
    fun doneCheckBoxFuntionLambda (position: Int) {
        val task =taskList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

      //binding.
    }
}