package com.eventos.eventosapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore

class SedeViewModel: ViewModel() {

    private  val db= FirebaseFirestore.getInstance()
    private val _sedeList = MutableLiveData<List<String>>()
    val sedeList: LiveData<List<String>> get() = _sedeList

    init {
        loadSedes()
    }

    private fun loadSedes() {
        db.collection("lugares")
            .get()
            .addOnSuccessListener { result ->
                val sedes = result.map { it.getString("sede") ?: "" }
                _sedeList.value = sedes
            }
            .addOnFailureListener { exception ->
                // Maneja el error aquí
            }
    }
}