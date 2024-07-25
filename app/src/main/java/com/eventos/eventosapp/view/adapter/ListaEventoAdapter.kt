package com.eventos.eventosapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.model.Evento
import com.eventos.eventosapp.view.viewholder.ListaEventoViewHolder

class ListaEventoAdapter: RecyclerView.Adapter<ListaEventoViewHolder>() {
    private var list = emptyList<Evento>()
    private var listFilter : List<Evento> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListaEventoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListaEventoViewHolder(inflater,parent)
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    override fun onBindViewHolder(holder: ListaEventoViewHolder, position: Int) {
        val evento=listFilter[position]
        holder.bind(evento)
    }

    fun setDatos(datos: List<Evento>) {
        list = datos
        listFilter = datos
        notifyDataSetChanged()
    }

    fun filtrarDatos (query: String) {
        listFilter = if (query.isEmpty()) {
            list
        } else {
            list.filter {
                it.nomevento.contains(query, ignoreCase = true) ||
                        it.nomprofe.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}