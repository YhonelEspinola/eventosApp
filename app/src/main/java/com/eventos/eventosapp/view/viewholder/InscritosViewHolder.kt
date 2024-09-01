package com.eventos.eventosapp.view.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.model.Inscrito

class InscritosViewHolder(inflater: LayoutInflater, viewGroup: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_inscritos,viewGroup,false)) {

        private var textNombreInscritos : TextView? = null
        private var textCorreoInscritos : TextView? = null

    init {
        textNombreInscritos = itemView.findViewById(R.id.textNombreInscritos)
        textCorreoInscritos = itemView.findViewById(R.id.textCorreoInscritos)


    }

    fun bind(inscrito: Inscrito, clickListener: (Inscrito) -> Unit) {
        textNombreInscritos?.text = inscrito.nombre
        textCorreoInscritos?.text = inscrito.correo_insti_estud

        // Configurar el listener para que cuando se haga clic en el elemento, se ejecute la acción
        itemView.setOnClickListener {
            clickListener(inscrito)
        }
    }


}