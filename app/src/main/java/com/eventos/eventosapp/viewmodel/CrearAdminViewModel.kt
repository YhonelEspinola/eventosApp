package com.eventos.eventosapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CrearAdminViewModel:ViewModel() {

    private  lateinit var  firebaseAuth: FirebaseAuth

    val adminRegisterStatus = MutableLiveData<Boolean>()
    val usuarioError = MutableLiveData<String>()
    val correoError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val mensajeError = MutableLiveData<String>()

    fun validarInformacion(usuario:String,correo: String,password: String) {
        if (usuario.isEmpty()){
            usuarioError.value = "Ingrese un Usuario"
            adminRegisterStatus.value = false
        }else if (correo.isEmpty()){
            correoError.value = "Ingrese su Correo"
            adminRegisterStatus.value = false
        }else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            correoError.value = "Correo no valido"
            adminRegisterStatus.value = false
        }else if (password.isEmpty()){
            passwordError.value = "Ingrese una contraseña"
            adminRegisterStatus.value = false
        }else if (password.length < 6){
            passwordError.value = "Contraseña muy corta, ingrese 6 o mas caracteres"
            adminRegisterStatus.value = false
        }else{
            registrarUsuario(usuario,correo,password)
        }
    }

    private fun registrarUsuario(usuario: String,correo: String, password: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(correo,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    insertarInfoBD(usuario,correo)
                }else{
                    adminRegisterStatus.value = false
                    mensajeError.value = "Error al registrar: ${task.exception?.message}"
                }
            }
    }

    private fun insertarInfoBD(usuario: String, correo: String) {
        val db = FirebaseFirestore.getInstance()

        val uidBD = firebaseAuth.uid

        val datosAdministrador = hashMapOf(
            "uid" to uidBD,
            "nombre" to usuario,
            "correo_insti_admin" to correo,
            "tipoUsuario" to "administrador"
        )

        if (uidBD != null) {
            db.collection("usuarios").document(uidBD).set(datosAdministrador)
                .addOnSuccessListener {
                    adminRegisterStatus.value = true
                }
                .addOnFailureListener { e ->
                    adminRegisterStatus.value = false
                    mensajeError.value = "Fallo el registro en la Base de Datos debido a ${e.message}"
                }
        } else {
            adminRegisterStatus.value = false
            mensajeError.value = "Error al obtener UID del usuario."
        }
    }
}