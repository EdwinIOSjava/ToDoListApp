package com.example.todolist.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.adapters.CategoryAdapter
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryDAO
import com.example.todolist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    lateinit var categoryDAO: CategoryDAO
    lateinit var categoryList: List<Category>

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




// usamos las func Lambda para que al darle click en un texto , podamos modificar el texto de la tarea.
        adapter = CategoryAdapter(
            emptyList(),
            ::showCategory,
            ::editCategory,
            ::deleteCategoryFuntionLambda // esta es la seguda funcion Lambda pero la hemos  bautizado y creado a lo ultimo para ejemplificar que se puede hacer asi
        )
        // asignamos el adapter al recycler view
        binding.recyclerView.adapter = adapter
        // asignamos el layout manager al recycler view
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // asignamos el listener al boton de añadir tarea
        binding.addCategoryButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        refreshData()

    }

    fun refreshData() {
        //categoryList = categoryDAO.findAll()// usamos la funcion findAll de la clase CategoryDAO para obtener todas las tareas de la base de datos
        categoryList = categoryDAO.findAll()
        adapter.updateItems(categoryList)// usamos la funcion updateItems de la clase CategoryAdapter para actualizar los datos del recycler view
    }

    fun showCategory(position: Int){
        val category = categoryList[position]

        val intent = Intent(this, TaskListActivity::class.java)
        intent.putExtra(TaskListActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }

    fun editCategory(position: Int){

        val category = categoryList[position]

        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CategoryActivity.CATEGORY_ID, category.id)
        startActivity(intent)

    }
    fun deleteCategoryFuntionLambda(position: Int) {
        // AQUI VA LA tercera FUNC LAMBDA que usaremos para Borrar la tarea al presionar el boton de borrar

        val category =categoryList[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion

        AlertDialog.Builder(this)
            .setTitle("Delete category")
            .setMessage("Are you sure you want to delete this category?")
            .setPositiveButton("Yes") { _, _ ->
                categoryDAO.delete(category) // usamos la funcion delete de la clase CategoryDAO para borrar la tarea de la base de datos
                refreshData() // por ultimo llamamos a la funcion que refresca los datos del recycler view
            }
            .setNegativeButton(
                android.R.string.cancel,
                null
            )// no haremos nada-- usamos R.string.cancel porque ya existe en el android  y el mismo lo traduce
            .setCancelable(false)// esto es para que si pulsamos fuera del dialogo no se cierre
            .show()
    }


    // en esta funcion Lambda vamos a modificar la tarea en un AlertDialog
    // aun no se usa
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
                categoryDAO.delete(category) // usamos la funcion delete de la clase CategoryDAO para borrar la tarea de la base de datos
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
            R.id.action_add -> {
                // Acción al hacer clic
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}//hola