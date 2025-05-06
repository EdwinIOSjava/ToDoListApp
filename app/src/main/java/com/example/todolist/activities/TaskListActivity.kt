package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.TaskAdapter
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
    }


lateinit var binding: ActivityTaskListBinding

lateinit var taskDAO: TaskDAO
lateinit var categoryDAO: CategoryDAO
lateinit var taskList: List<Task>

lateinit var adapter: TaskAdapter
lateinit var category: Category


override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val id = intent.getLongExtra(CATEGORY_ID, -1L)
        taskDAO = TaskDAO(this)
    categoryDAO=CategoryDAO(this)
    category=categoryDAO.findById(id)!!// aqui obtenemos la categoria que se ha pulsado en el recycler view apartir de su id


        // asignamos al adapter la lista de tareas vacia y una funcion lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento
        // en el recycler view

// usamos las func Lambda para que al darle click en un texto , podamos modificar el texto de la tarea.
        adapter = TaskAdapter(
            emptyList(),
            ::editTask,
            ::deleteTaskFuntionLambda, // esta es la seguda funcion Lambda pero la hemos  bautizado y creado a lo ultimo para ejemplificar que se puede hacer asi
            ::checkTask
        )
        // asignamos el adapter al recycler view
        binding.recyclerView.adapter = adapter
        // asignamos el layout manager al recycler view
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // asignamos el listener al boton de añadir tarea
        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        refreshData()

    }
    fun checkTask(position: Int) {
        val task = taskList[position]

        task.done = !task.done
        taskDAO.update(task)
        adapter.notifyItemChanged(position)
        refreshData()
    }
    private fun refreshData() {
        //taskList = taskDAO.findAll()// usamos la funcion findAll de la clase TaskDAO para obtener todas las tareas de la base de datos
        taskList = taskDAO.findAllByCategory(category)
        adapter.updateItems(taskList)// usamos la funcion updateItems de la clase TaskAdapter para actualizar los datos del recycler view
    }

    fun deleteTaskFuntionLambda(position: Int) {
        // AQUI VA LA SEGUNDA FUNC LAMBDA que usaremos para Borrar la tarea al presionar el boton de borrar

        val task =
            taskList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

        AlertDialog.Builder(this)
            .setTitle("Delete task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Yes") { _, _ ->
                taskDAO.delete(task) // usamos la funcion delete de la clase TaskDAO para borrar la tarea de la base de datos
                refreshData() // por ultimo llamamos a la funcion que refresca los datos del recycler view
            }
            .setNegativeButton(
                android.R.string.cancel,
                null
            )// no haremos nada-- usamos R.string.cancel porque ya existe en el android  y el mismo lo traduce
            .setCancelable(false)// esto es para que si pulsamos fuera del dialogo no se cierre
            .show()
    }
    fun editTask(position: Int){
        val task = taskList[position]

        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, task.id)
        intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
        startActivity(intent)

    }

    fun doneCheckBoxFuntionLambda(position: Int) {
        val task =
            taskList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

        task.done =
            !task.done // aqui cambiamos el valor de la variable done de la tarea que se ha pulsado en el recycler view
        taskDAO.update(task)// usamos la funcion update de la clase TaskDAO para actualizar la tarea en la base de datos
        refreshData()// por ultimo llamamos a la funcion que refresca los datos del recycler view
    }

    // en esta funcion Lambda vamos a modificar la tarea en un AlertDialog
    // aun no se usa
    fun modifyTask(position: Int) {

        // aqui vamos a modificar la tarea en un AlertDialog
        val task =
            taskList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

        val textoEditado = EditText(this)
        textoEditado.setText(task.title)
        textoEditado.setSelection(textoEditado.text.length)

        AlertDialog.Builder(this)
            .setTitle("Edit task")
            .setView(textoEditado)
            .setPositiveButton("Guardar") { _, _ ->
                taskDAO.delete(task) // usamos la funcion delete de la clase TaskDAO para borrar la tarea de la base de datos
                refreshData() // por ultimo llamamos a la funcion que refresca los datos del recycler view
            }
            .setNegativeButton(
                android.R.string.cancel,
                null
            )// no haremos nada-- usamos R.string.cancel porque ya existe en el android  y el mismo lo traduce
            .setCancelable(false)// esto es para que si pulsamos fuera del dialogo no se cierre
            .show()
    }
}