package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.CategoryAdapter
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.data.Task
import com.example.todolist.data.TaskDAO
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.data.CategoryWithTasks


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var categoryDAO: CategoryDAO
    lateinit var taskDAO: TaskDAO
    lateinit var categoryList: List<Category>
    lateinit var categoryWithTasksList : List<CategoryWithTasks>

    lateinit var adapter: CategoryAdapter


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
        categoryDAO = CategoryDAO(this)
        // asignamos al adapter la lista de tareas vacia y una funcion lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento
        // en el recycler view
        //supportActionBar?.title = "Mis categorias"

        setSupportActionBar(findViewById(R.id.myToolbar))// AGREGO EL APPBAR CREADO EN EL XML
        supportActionBar?.setDisplayShowTitleEnabled(false)// ESTO  OCULTA EL TEXTOQ UE ME SALE POR DEFECTO

         categoryWithTasksList = buildCategoryWithTasksList()

// usamos las func Lambda para que al darle click en un texto , podamos modificar el texto de la tarea.
        adapter = CategoryAdapter(
            categoryWithTasksList,
            ::showCategory,
            ::modifyCategory,
            )

        // asignamos el adapter al recycler view
        binding.rvCategories.adapter = adapter
        // asignamos el layout manager al recycler view
        binding.rvCategories.layoutManager = GridLayoutManager(this,2)

        // asignamos el listener al boton de añadir tarea
        binding.addCategoryButton.setOnClickListener {
            createCategory()
        }
    }

    override fun onResume() {
        super.onResume()

        refreshData()

    }

    fun refreshData() {

        //categoryList = categoryDAO.findAll()// usamos la funcion findAll de la clase CategoryDAO para obtener todas las tareas de la base de datos
        categoryWithTasksList = buildCategoryWithTasksList()
        adapter.updateItems(categoryWithTasksList)// usamos la funcion updateItems de la clase CategoryAdapter para actualizar los datos del recycler view
        Log.i("MainActivity", "refreshData + ${categoryWithTasksList}")
    }

    fun createCategory() {val dialogView = layoutInflater.inflate(R.layout.dialog_add_category, null)
        val editText = dialogView.findViewById<EditText>(R.id.categoryEditText)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)
            .create()

// Botones
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
        val btnSave = dialogView.findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val categoryName = editText.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                // Crear la categoría y guardarla en SQLite
                val newCategory = Category(-1, categoryName)
                categoryDAO.insert(newCategory)
                refreshData() // Para que se actualice la lista
            }
            dialog.dismiss() // Cierra el diálogo después de guardar
        }

        btnCancel.setOnClickListener {
            dialog.dismiss() // Cierra el diálogo si se cancela
        }

        dialog.show()

    }


    fun showCategory(position: Int){
        val category = categoryList[position]

        val intent = Intent(this ,TaskListActivity::class.java)
        intent.putExtra(TaskListActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }

    fun modifyCategory(position: Int) {

        // aqui vamos a modificar la tarea en un AlertDialog
        val category =
            categoryList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

        val textoEditado = EditText(this)
        textoEditado.setText(category.title)
        textoEditado.setSelection(textoEditado.text.length)

        AlertDialog.Builder(this)
            .setTitle("Edit category")
            .setView(textoEditado)
            .setPositiveButton("Guardar") { _, _ ->
                categoryDAO.update(category) // usamos la funcion delete de la clase CategoryDAO para borrar la tarea de la base de datos
                refreshData() // por ultimo llamamos a la funcion que refresca los datos del recycler view
            }
            .setNegativeButton(
                android.R.string.cancel,
                null
            )// no haremos nada-- usamos R.string.cancel porque ya existe en el android  y el mismo lo traduce
            .setCancelable(false)// esto es para que si pulsamos fuera del dialogo no se cierre
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.folders -> {
                // Acción al hacer clic
                Toast.makeText(this, "Has pulsado en folders", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun buildCategoryWithTasksList(): List<CategoryWithTasks> {
        categoryDAO = CategoryDAO(this)
        taskDAO = TaskDAO(this)

         categoryList = categoryDAO.findAll() // o como sea tu métxodo para obtener todas las categorías

       return  categoryList.map { category ->

            val tasks = taskDAO.findAllByCategory(category) // le enviamos la categoria en la que estamos para que nos devuelva todas las tareas de esa categoria
           Log.i("MainActivity", "Categoría: ${category.title}, Tareas: $tasks") // Agregado para depuración
            CategoryWithTasks(
                id = category.id,
                title = category.title,
                tasks = tasks
            )
        }
    }



}