package com.eventos.eventosapp.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eventos.eventosapp.model.Evento
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class EventoViewModel : ViewModel(){

    private val storage = Firebase.storage
    private  val db= FirebaseFirestore.getInstance()
    val eventoCreado = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val listEventos = MutableLiveData<List<Evento>>()

    fun crearEvento(nomevento:String,categoria:String, sede:String,
                    fechayhora: Timestamp?, horafin: String,
                    maxparticipantes:Long, descripcion:String,
                    imgevento:Uri?, imgprofe:Uri?,nomprofe:String,
                    infoprofe:String, estadoevento:Boolean) {


        val filenameEvento = UUID.randomUUID().toString()
        val refEvento = storage.reference.child("$filenameEvento")
        val uploadTaskEvento = refEvento.putFile(imgevento!!)

        val filenameProfe = UUID.randomUUID().toString()
        val refProfe = storage.reference.child("$filenameProfe")
        val uploadTaskProfe = refProfe.putFile(imgprofe!!)

        Tasks.whenAllSuccess<Uri>(uploadTaskEvento.continueWithTask { refEvento.downloadUrl },
            uploadTaskProfe.continueWithTask { refProfe.downloadUrl })
            .addOnSuccessListener { uris ->
                val imgEvento = uris[0].toString()
                val imgProfe = uris[1].toString()

                db.collection("lugares")
                    .whereEqualTo("sede", sede)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                            val lugarDoc = querySnapshot.documents[0] // Suponiendo que solo hay uno
                            // Obtener el campo "ubicacion" del documento de lugar
                            val ubicacion = lugarDoc.getGeoPoint("ubicacion")

                            val data = hashMapOf(
                                "nomevento" to nomevento,
                                "categoria" to categoria,
                                "sede" to sede,
                                "fechayhora" to fechayhora,
                                "horafin" to horafin,
                                "maxparticipantes" to maxparticipantes,
                                "actualparticipantes" to 0,
                                "descripcion" to descripcion,
                                "imgevento" to imgEvento,
                                "imgprofe" to imgProfe,
                                "nomprofe" to nomprofe,
                                "infoprofe" to infoprofe,
                                "estadoevento" to estadoevento,
                                "ubicacion" to ubicacion // Asignar ubicacion a coordenadas
                            )

                            db.collection("eventos")
                                .add(data)
                                .addOnSuccessListener { documentReference ->
                                    eventoCreado.value = true
                                }
                                .addOnFailureListener { e ->
                                    errorMessage.value = "Error al guardar el evento: ${e.message}"
                                }
                    }
                    .addOnFailureListener { e ->
                        errorMessage.value = "Error al obtener las coordenadas: ${e.message}"
                    }
            }
            .addOnFailureListener {
                errorMessage.value = "Error al subir las imágenes: ${it.message}"
            }

    }

    fun validar(nomevento:String, categoria:String ,sede:String,
                fechayhora: String, horafin: String,
                maxparticipantes:Long, descripcion:String,
                imgevento:Uri?, imgprofe:Uri?,nomprofe:String,
                infoprofe:String): String? {

        return when {
            imgevento == null -> "La imagen del evento está vacía"
            nomevento.isEmpty() -> "El nombre del evento está vacío"
            categoria.isEmpty() -> "Elige una categoria"
            sede.isEmpty() -> "Elige una ubicación"
            fechayhora.isEmpty() -> "La fecha y hora de inicio están vacías"
            horafin.isEmpty() -> "La hora de fin está vacía"
            maxparticipantes <= 0 -> "El número máximo de participantes debe ser mayor que cero"
            descripcion.isEmpty() -> "La descripción está vacía"
            imgprofe == null -> "La imagen del profesor está vacía"
            nomprofe.isEmpty() -> "El nombre del profesor está vacío"
            infoprofe.isEmpty() -> "La información del profesor está vacía"
            else -> null
        }
    }

    fun listarEvento(){
        db.collection("eventos")
            .orderBy("fechayhora", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var newList = arrayListOf<Evento>()
                for(document in querySnapshot){
                    val data = document.data
                    val nomevento = data["nomevento"] as? String ?: ""
                    val categoria = data["categoria"] as? String ?: ""
                    val sede = data["sede"] as? String ?: ""
                    val fechayhora = data["fechayhora"] as? Timestamp
                    val horafin = data["horafin"] as? String ?: ""
                    val maxparticipantes = data["maxparticipantes"] as? Long ?: 0
                    val actualparticipantes = data["actualparticipantes"] as? Long ?: 0
                    val descripcion = data["descripcion"] as? String ?: ""
                    val imgevento = data["imgevento"] as? String ?: ""
                    val imgprofe = data["imgprofe"] as? String ?: ""
                    val nomprofe = data["nomprofe"] as? String ?: ""
                    val infoprofe = data["infoprofe"] as? String ?: ""
                    val estadoevento = data["estadoevento"] as? Boolean ?: false
                    val ubicacion = data["ubicacion"] as? GeoPoint?:GeoPoint(0.0, 0.0)
                    val codigo = document.id

                    val modelo= Evento(nomevento,categoria,sede,fechayhora,horafin,maxparticipantes,actualparticipantes,
                        descripcion,imgevento,imgprofe,nomprofe,infoprofe,estadoevento,ubicacion,codigo)
                    newList.add((modelo))

                }
                listEventos.value=newList
            }
    }

    fun listarEventoPorFiltros(sede:String,categoria:String){

        var query:Query = db.collection("eventos")

        if (!sede.isNullOrEmpty() ) {
            query = query.whereEqualTo("sede", sede)
        }

        if (!categoria.isNullOrEmpty()) {
            query = query.whereEqualTo("categoria", categoria)
        }

        query.whereEqualTo("estadoevento", true)
            .orderBy("fechayhora", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { querySnapshot ->
                var newList = arrayListOf<Evento>()
                for(document in querySnapshot){
                    val data = document.data
                    val nomevento = data["nomevento"] as? String ?: ""
                    val categoria = data["categoria"] as? String ?: ""
                    val sede = data["sede"] as? String ?: ""
                    val fechayhora = data["fechayhora"] as? Timestamp
                    val horafin = data["horafin"] as? String ?: ""
                    val maxparticipantes = data["maxparticipantes"] as? Long ?: 0
                    val actualparticipantes = data["actualparticipantes"] as? Long ?: 0
                    val descripcion = data["descripcion"] as? String ?: ""
                    val imgevento = data["imgevento"] as? String ?: ""
                    val imgprofe = data["imgprofe"] as? String ?: ""
                    val nomprofe = data["nomprofe"] as? String ?: ""
                    val infoprofe = data["infoprofe"] as? String ?: ""
                    val estadoevento = data["estadoevento"] as? Boolean ?: false
                    val ubicacion = data["ubicacion"] as? GeoPoint?:GeoPoint(0.0, 0.0)
                    val codigo = document.id

                    val modelo= Evento(nomevento,categoria,sede,fechayhora,horafin,maxparticipantes,actualparticipantes,
                        descripcion,imgevento,imgprofe,nomprofe,infoprofe,estadoevento,ubicacion,codigo)
                    newList.add((modelo))

                }
                listEventos.value=newList
            }
    }
}