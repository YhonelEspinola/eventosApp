package com.eventos.eventosapp.view.viewholder

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.model.Evento
import com.eventos.eventosapp.view.activity.DetalleEventoActivity
import com.squareup.picasso.Picasso
import java.util.Locale

class ListaEventoViewHolder(inflater: LayoutInflater, viewGroup: ViewGroup):
    RecyclerView.ViewHolder(inflater.inflate(R.layout.item_eventos,viewGroup,false)) {

    private var imagen: ImageView?=null
    private var categoria: TextView?=null
    private var titulo: TextView?=null
    private var sedeDireccion: TextView?=null
    private var fechaHora: TextView?=null
    private var horaFin: TextView?=null
    private var participantesActuales: TextView?=null
    private var participantesMax: TextView?=null
    private var item_evento: LinearLayout?=null

    init{
        imagen = itemView.findViewById(R.id.imagen)
        categoria = itemView.findViewById(R.id.categoria)
        titulo= itemView.findViewById(R.id.titulo)
        sedeDireccion= itemView.findViewById(R.id.sedeDireccion)
        fechaHora = itemView.findViewById(R.id.fechaHora)
        horaFin=itemView.findViewById(R.id.horaFin)
        participantesActuales=itemView.findViewById(R.id.participantesActuales)
        participantesMax=itemView.findViewById(R.id.participantesMax)
        item_evento=itemView.findViewById(R.id.item_evento)


    }

    fun bind(evento: Evento){

        val dateFormat = SimpleDateFormat("dd 'de' MMM. hh:mm a", Locale("es", "ES"))
        val fechaFormateada = evento.fechayhora?.toDate()?.let { dateFormat.format(it) }

        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val horaFinDate = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(evento.horafin)
        val horaFinFormateada = horaFinDate?.let { timeFormat.format(it) }

        imagen?.let{
            Picasso.get()
                .load(evento.imgevento)
                .placeholder(R.drawable.img_carga)
                .error(R.drawable.img_error)
                .into(it)
        }
        categoria?.text=evento.categoria
        titulo?.text=evento.nomevento
        sedeDireccion?.text=evento.sede
        fechaHora?.text=fechaFormateada
        horaFin?.text=horaFinFormateada
        participantesActuales?.text= evento.actualparticipantes.toString()
        participantesMax?.text= evento.maxparticipantes.toString()

        item_evento?.setOnClickListener {
            val intent = Intent(itemView.context, DetalleEventoActivity::class.java).apply {
                putExtra("nomevento", evento.nomevento)
                putExtra("categoria", evento.categoria)
                putExtra("sede", evento.sede)
                putExtra("fechayhora", fechaFormateada)
                putExtra("horafin", horaFinFormateada)
                putExtra("maxparticipantes", evento.maxparticipantes)
                putExtra("actualparticipantes", evento.actualparticipantes)
                putExtra("descripcion", evento.descripcion)
                putExtra("imgevento", evento.imgevento)
                putExtra("imgprofe", evento.imgprofe)
                putExtra("nomprofe", evento.nomprofe)
                putExtra("infoprofe", evento.infoprofe)
                putExtra("latitud", evento.ubicacion.latitude)
                putExtra("longitud", evento.ubicacion.longitude)
                putExtra("codigo", evento.codigo)
                putExtra("valor", true)
            }
            itemView.context.startActivity(intent)
        }


    }
}