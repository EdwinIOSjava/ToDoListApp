package com.example.todolist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todolist.data.Category
import com.example.todolist.databinding.ItemCategoryBinding
import com.example.todolist.utils.addStrikethrough
import com.example.todolist.utils.changeColorText

class CategoryAdapter(
    var items: List<Category>,// creamos una variable items de tipo List<Category> que sera la lista de tareas que se mostrarán en el recycler view
    val onClick: (Int) -> Unit,// esta es una funcion lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento en el recycler view
    val onEdit: (Int) -> Unit,
    val onDelete: (Int) -> Unit // onDelete es el nombre de esta funci Lambda que se ejecuta cuando se hace click en el boton de borrar
) : Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = items[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion
        holder.render(category)//
        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.itemView.setOnLongClickListener {
            onEdit(position)
            true
        }
        holder.binding.deleteButton.setOnClickListener{// aqui escuchamos el click en el boton de borrar
            onDelete(position)// aqui llamamos a la funcion que se ejecuta cuando se hace click en el boton de borrar
            // en este caso la funcion onDelete  fue el nombre que le dimos como funcion lambda en el constructor de la clase CategoryAdapter
        }

    }

    fun updateItems(items: List<Category>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class CategoryViewHolder(val binding: ItemCategoryBinding) : ViewHolder(binding.root) {

    fun render(category: Category) {
            binding.titleTextView.text = category.title.addStrikethrough()
    }
}