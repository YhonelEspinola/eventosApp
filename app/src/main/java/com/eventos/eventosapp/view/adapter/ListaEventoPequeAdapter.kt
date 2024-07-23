package com.eventos.eventosapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.model.Evento
import com.eventos.eventosapp.view.viewholder.ListaEventoPequeViewHolder

class ListaEventoPequeAdapter(): RecyclerView.Adapter<ListaEventoPequeViewHolder>()  {

    private var list= emptyList<Evento>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaEventoPequeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListaEventoPequeViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListaEventoPequeViewHolder, position: Int) {
        val evento=list[position]
        holder.bind(evento)
    }

    fun setDatos(datos: List<Evento>) {
        list = datos
        notifyDataSetChanged()
    }
}