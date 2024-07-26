package com.eventos.eventosapp.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.Timestamp

class EditarEventoViewModel: ViewModel() {

    private val storage = Firebase.storage
    private val db = FirebaseFirestore.getInstance()

    private val _eventUpdateStatus = MutableLiveData<Boolean>()
    val eventUpdateStatus: LiveData<Boolean> get() = _eventUpdateStatus

    fun uploadImageToFirebaseStorage(imageUri: Uri, path: String, onSuccess: (String) -> Unit) {
        val storageRef = storage.reference.child(path)
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener {
            Log.e("FirebaseStorage", "Image upload failed: ${it.message}")
        }
    }
    fun updateEventInFirestore(
        codigo: String,
        nomevento: String,
        categoria: String,
        sede: String,
        fechayhora: Timestamp,
        horafin: String,
        maxParticipantes: Int,
        descripcion: String,
        imgEvento: String,
        imgProfesor: String,
        nomProfe: String,
        infoProfe: String,
        estadoEvento: Boolean
    ){
        val data = HashMap<String, Any>()
        data["nomevento"] = nomevento
        data["categoria"] = categoria
        data["sede"] = sede
        data["fechayhora"] = fechayhora
        data["horafin"] = horafin
        data["maxparticipantes"] = maxParticipantes
        data["descripcion"] = descripcion
        data["imgevento"] = imgEvento
        data["imgprofe"] = imgProfesor
        data["nomprofe"] = nomProfe
        data["infoprofe"] = infoProfe
        data["estadoevento"] = estadoEvento

        db.collection("eventos").document(codigo)
            .update(data)
            .addOnSuccessListener {
                _eventUpdateStatus.value = true
            }
            .addOnFailureListener {
                _eventUpdateStatus.value = false
            }
    }
}