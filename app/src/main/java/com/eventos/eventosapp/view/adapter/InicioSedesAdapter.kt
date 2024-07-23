package com.eventos.eventosapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.model.Sede
import com.eventos.eventosapp.view.fragment.InicioFragment
import com.eventos.eventosapp.view.viewholder.InicioSedesViewHolder

class InicioSedesAdapter(val list: List<Sede>, private val listener: InicioFragment): RecyclerView.Adapter<InicioSedesViewHolder>() {
    private var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InicioSedesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return InicioSedesViewHolder(inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: InicioSedesViewHolder, position: Int) {
        val sede = list[position]
        var pos:Int = position
        holder.bind(sede)
        if (pos == selectedPosition) {
            holder.btnSedes?.setBackgroundResource(R.drawable.design_filter_sede_background_selected)
            holder.btnSedes?.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.btnSedes?.setBackgroundResource(R.drawable.design_filter_sede_background_default)
            holder.btnSedes?.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }
        holder.btnSedes?.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = pos
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            listener.onSedeClick(sede)
        }
    }
}