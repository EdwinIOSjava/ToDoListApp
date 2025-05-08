package com.example.todolist.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todolist.data.Category
import com.example.todolist.data.CategoryWithTasks
import com.example.todolist.databinding.ItemCategoryBinding
import com.example.todolist.utils.addStrikethrough
import com.example.todolist.utils.changeColorText

class CategoryAdapter(

    var items: List<CategoryWithTasks>,// creamos una variable items de tipo List<Category> que sera la lista de tareas que se mostrarán en el recycler view
    val onClick: (Int) -> Unit,// esta es una funcion lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento en el recycler view
    val onEdit: (Int) -> Unit
) : Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = items[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion
        Log.i("CategoryAdapter", "onBindViewHolder: $category")


        holder.render(category)

        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            onEdit(position)
            true
        }

    }

    fun updateItems(items: List<CategoryWithTasks>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class CategoryViewHolder(val binding: ItemCategoryBinding) : ViewHolder(binding.root) {

    fun render(categoryWithTask: CategoryWithTasks) {
        binding.tvCategory.text = categoryWithTask.title

        val tareasText = categoryWithTask.tasks.joinToString(separator = "\n") { "- ${it.title}" }
        binding.tvTasks.text = tareasText

        // Si no hay tareas, puedes ocultar el TextView o mostrar un mensaje opcional:
        binding.tvTasks.visibility = if (tareasText.isBlank()) View.GONE else View.VISIBLE
    }

}