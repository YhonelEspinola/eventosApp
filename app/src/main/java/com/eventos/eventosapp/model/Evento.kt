package com.eventos.eventosapp.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Evento (
    val nomevento:String,
    val categoria:String,
    val sede:String,
    val fechayhora: Timestamp?,
    val horafin: String,
    val maxparticipantes:Long,
    val actualparticipantes:Long,
    val descripcion:String,
    val imgevento:String,
    val imgprofe:String,
    val nomprofe:String,
    val infoprofe:String,
    val estadoevento:Boolean,
    val ubicacion:GeoPoint,
    val codigo:String
)