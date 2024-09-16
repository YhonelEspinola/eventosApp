package com.eventos.eventosapp.viewmodel

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class ParticiparViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val dbCollecion = firestore.collection("inscritos")
    private val auth = FirebaseAuth.getInstance()

    val participarStatus = MutableLiveData<Boolean>()
    val mensajeError = MutableLiveData<String>()

    fun comprobarSesion() : Boolean{
        return auth.currentUser != null
    }

    fun participar(idevento: String){
        val currentUser = auth.currentUser
        if(currentUser != null){
            val uid = currentUser.uid
            dbCollecion.whereEqualTo("uid", uid)
                .whereEqualTo("idevento", idevento)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty){
                        mensajeError.value = "Ya estás inscrito en este evento"
                        participarStatus.value = false
                    }else{
                        firestore.collection("usuarios").document(uid).get()
                            .addOnSuccessListener { document ->
                                if(document != null && document.exists()){
                                    val nombre = document.getString("nombre") ?: ""
                                    val carrera = document.getString("carrera") ?: ""
                                    val sede = document.getString("sede") ?: ""
                                    val correo_insti_estud = document.getString("correo_insti_estud") ?: ""

                                    val data = hashMapOf(
                                        "nombre" to nombre,
                                        "carrera" to carrera,
                                        "idevento" to idevento,
                                        "uid" to uid,
                                        "sede" to sede,
                                        "correo_insti_estud" to correo_insti_estud
                                    )
                                    dbCollecion.add(data)
                                        .addOnSuccessListener {
                                            firestore.collection("eventos").document(idevento)
                                                .update("actualparticipantes", FieldValue.increment(1))
                                                .addOnSuccessListener {
                                                    participarStatus.value = true
                                                }.addOnFailureListener{e ->
                                                    mensajeError.value = "Error al actualizar el número de participantes: ${e.message}"
                                                    participarStatus.value = false
                                                }
                                        }
                                        .addOnFailureListener{ e ->
                                            mensajeError.value = "Error al participar: ${e.message}"
                                            participarStatus.value = false
                                        }
                                }
                            }
                    }
                }
                .addOnFailureListener{ e ->
                    mensajeError.value = "Error al obtener datos del usuario: ${e.message}"
                    participarStatus.value = false
                }
        }else{
            mensajeError.value ="Usuario no ha iniciado sesion"
            participarStatus.value = false
        }
    }



}