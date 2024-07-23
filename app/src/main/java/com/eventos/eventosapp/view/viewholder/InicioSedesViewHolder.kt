package com.eventos.eventosapp.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.model.Sede

class InicioSedesViewHolder (inflater: LayoutInflater, viewGroup: ViewGroup): RecyclerView.ViewHolder(inflater.inflate(
    R.layout.item_sedes,viewGroup,false)){

    var btnSedes : TextView? = null

    init {
        btnSedes = itemView.findViewById(R.id.btnSedes)
    }

    fun bind(sede: Sede){
        btnSedes?.text= sede.nomsede

    }
}