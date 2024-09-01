package com.eventos.eventosapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.model.Inscrito
import com.eventos.eventosapp.view.viewholder.InscritosViewHolder

class InscritosAdapter(val inscritosList: List<Inscrito>,  private val clickListener: (Inscrito) -> Unit) : RecyclerView.Adapter<InscritosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscritosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return InscritosViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return inscritosList.size
    }

    override fun onBindViewHolder(holder: InscritosViewHolder, position: Int) {
        val inscrito = inscritosList[position]
        holder.bind(inscrito, clickListener)
    }
}