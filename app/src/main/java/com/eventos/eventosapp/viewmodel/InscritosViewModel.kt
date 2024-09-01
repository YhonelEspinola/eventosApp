package com.eventos.eventosapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eventos.eventosapp.model.Inscrito
import com.google.firebase.firestore.FirebaseFirestore

class InscritosViewModel : ViewModel() {

    private val _inscritosList = MutableLiveData<List<Inscrito>>()
    val inscritosList: LiveData<List<Inscrito>> get() = _inscritosList

    private val firestore = FirebaseFirestore.getInstance()

    fun cargarInscritos(idevento: String) {
        Log.d("InscritosViewModel", "idevento pasado a Firestore: $idevento")
        firestore.collection("inscritos")
            .whereEqualTo("idevento", idevento)
            .get()
            .addOnSuccessListener { snapshot ->
                var newList = arrayListOf<Inscrito>()
                for(document in snapshot){
                    val data = document.data
                    val nombre = data["nombre"] as? String?: ""
                    val carrera = data["carrera"] as? String?:""
                    val idevento = data["idevento"] as? String?: ""
                    val uid = data["uid"] as? String?:""
                    val sede = data["sede"] as? String?:""
                    val correo_insti_estud = data["correo_insti_estud"] as? String?:""
                    val codigo = document.id

                val modelo= Inscrito(nombre,carrera,idevento,uid, sede, correo_insti_estud, codigo)
                    newList.add(modelo)
                    Log.d("InscritosViewModel", "Inscrito añadido: $modelo")
                }
                Log.d("InscritosViewModel", "Número total de inscritos encontrados: ${newList.size}")
                _inscritosList.value=newList
                Log.d("InscritosViewModel", "Lista final asignada al LiveData: $newList")
            }
            .addOnFailureListener { e ->
                Log.e("InscritosViewModel", "Error al cargar inscritos", e)
                _inscritosList.value = emptyList() // en caso de error devolvemos lista vacía
            }
    }


}