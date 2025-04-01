package com.example.todolist.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todolist.data.Task
import com.example.todolist.databinding.ItemTaskBinding

class TaskAdapter(
    var items: List<Task>,// creamos una variable items de tipo List<Task> que sera la lista de tareas que se mostrarán en el recycler view
    val onClick: (Int) -> Unit,// esta es una funcion lambda que se ejecuta cuando se hace click en un elemento del recycler view y recibe la posicion del elemento en el recycler view
    val onDelete: (Int) -> Unit, // onDelete es el nombre de esta funci Lambda que se ejecuta cuando se hace click en el boton de borrar
    val onCheckBox: (Int) ->Unit// se oprie el checkbox
) : Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position] // obtenemos la tarea que se ha pulsado en el recycler view apartir de su posicion
        holder.render(task)//
        holder.itemView.setOnClickListener {
            onClick(position)
        }
        holder.binding.deleteButton.setOnClickListener{// aqui escuchamos el click en el boton de borrar
            onDelete(position)// aqui llamamos a la funcion que se ejecuta cuando se hace click en el boton de borrar
            // en este caso la funcion onDelete  fue el nombre que le dimos como funcion lambda en el constructor de la clase TaskAdapter
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { _ , _ ->
            if (holder.binding.doneCheckBox.isPressed) {
                onCheckBox(position)
            }
        }

    }

    fun updateItems(items: List<Task>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {

    fun render(task: Task) {
        binding.titleTextView.text = task.title
        binding.doneCheckBox.isChecked = task.done
    }
}