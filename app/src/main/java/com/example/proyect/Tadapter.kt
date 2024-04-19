package com.example.proyect

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class Tadapter(private val context: Context, private var tareas: List<Tarea>) :
    RecyclerView.Adapter<Tadapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(tarea: Tarea?)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener?) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.tarea, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tarea = tareas[position]
        val key = tarea.key
        holder.textViewName.text = tarea.name
        holder.textViewDetails.text = tarea.detalles
        holder.textViewDate.text = tarea.date
        holder.textViewNombre.text = tarea.nombre
        holder.cardView.setOnClickListener {
            if (listener != null) {
                listener!!.onItemClick(tarea)
            }
        }
    }

    override fun getItemCount(): Int {
        return tareas.size
    }

    fun setTareas(tareas: List<Tarea>) {
        this.tareas = tareas.toMutableList() // Create a mutable copy
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cardView: CardView
        var textViewName: TextView
        var textViewDetails: TextView
        var textViewDate: TextView
        var textViewNombre: TextView

        init {
            cardView = itemView as CardView
            textViewName = itemView.findViewById(R.id.nombre)
            textViewDetails = itemView.findViewById(R.id.infod)
            textViewDate = itemView.findViewById(R.id.fecha)
            textViewNombre = itemView.findViewById(R.id.Nombre1)
        }
    }
}
