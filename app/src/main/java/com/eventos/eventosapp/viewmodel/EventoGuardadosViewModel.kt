package com.eventos.eventosapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eventos.eventosapp.model.Evento
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint

class EventoGuardadosViewModel: ViewModel() {
    private  val db= FirebaseFirestore.getInstance()
    val listEventos = MutableLiveData<List<Evento>>()
    val eventoverificado = MutableLiveData<Boolean>()
    val Message = MutableLiveData<String>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    val uid = currentUser?.uid

    fun VerificarEvento(eid:String){
        db.collection("e_guardados")
            .whereEqualTo("eid", eid)
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    eventoverificado.value = false
                } else {
                    eventoverificado.value = true
                }
            }
            .addOnFailureListener { exception ->
                eventoverificado.value = false
            }
    }

    fun GuardarEvento(eid:String){
        val data = hashMapOf(
            "eid" to eid,
            "uid" to uid
        )
        db.collection("e_guardados")
            .add(data)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }
    }

    fun QuitarEvento(eid:String){

        if (uid != null) {
            db.collection("e_guardados")
                .whereEqualTo("eid", eid)
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        db.collection("e_guardados").document(document.id).delete()
                            .addOnSuccessListener {
                                // Documento eliminado con éxito
                            }
                            .addOnFailureListener { exception ->
                                // Manejar error al eliminar documento
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar error al obtener documentos
                }
        }
    }

    fun listarEvento(){
        if (uid != null) {
            db.collection("e_guardados")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { documents ->
                    val eids = documents.map { it.getString("eid") }
                    if (eids.isNotEmpty()) {
                        db.collection("eventos")
                            .whereIn(FieldPath.documentId(), eids)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                val newList = arrayListOf<Evento>()
                                for (document in querySnapshot) {
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
                                    val ubicacion = data["ubicacion"] as? GeoPoint ?: GeoPoint(0.0, 0.0)
                                    val codigo = document.id

                                    val modelo= Evento(nomevento,categoria,sede,fechayhora,horafin,maxparticipantes,actualparticipantes,
                                        descripcion,imgevento,imgprofe,nomprofe,infoprofe,estadoevento,ubicacion,codigo)
                                    newList.add(modelo)
                                }
                                listEventos.value = newList
                            }
                            .addOnFailureListener { exception ->
                                // Manejar error
                                listEventos.value = emptyList()
                            }
                    } else {
                        // Si no hay eids, retornar lista vacía
                        listEventos.value = emptyList()
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejar error
                    listEventos.value = emptyList()
                }
        } else {
            // Si no hay usuario logueado, retornar lista vacía
            listEventos.value = emptyList()
        }
    }
}