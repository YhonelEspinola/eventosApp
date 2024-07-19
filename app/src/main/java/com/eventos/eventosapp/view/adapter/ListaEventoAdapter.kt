package com.eventos.eventosapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.model.Evento
import com.eventos.eventosapp.view.viewholder.ListaEventoViewHolder

class ListaEventoAdapter(): RecyclerView.Adapter<ListaEventoViewHolder>()  {

    private var list= emptyList<Evento>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaEventoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListaEventoViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListaEventoViewHolder, position: Int) {
        val evento=list[position]
        holder.bind(evento)
    }

    fun setDatos(datos: List<Evento>) {
        list = datos
        notifyDataSetChanged()
    }
}