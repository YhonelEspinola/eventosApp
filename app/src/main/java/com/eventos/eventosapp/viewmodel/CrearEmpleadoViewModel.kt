package com.eventos.eventosapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CrearEmpleadoViewModel: ViewModel() {
    private  lateinit var  firebaseAuth: FirebaseAuth
    private var db = FirebaseFirestore.getInstance()

    val empRegisterStatus = MutableLiveData<Boolean>()
    val usuarioError = MutableLiveData<String>()
    val correoError = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()
    val mensajeError = MutableLiveData<String>()

    val sedeList = MutableLiveData<List<String>>()
    val carreraList = MutableLiveData<List<String>>()

    init {
        loadSedes()
        loadCarreras()
    }

    private fun loadSedes() {
        db.collection("lugares")
            .get()
            .addOnSuccessListener { result ->
                val sedes = result.mapNotNull { it.getString("sede")?:"" }
                sedeList.value = sedes
            }
            .addOnFailureListener { exception ->
                mensajeError.value = "Error al cargar las sedes: ${exception.message}"
            }
    }

    private fun loadCarreras() {
        db.collection("carrera")
            .get()
            .addOnSuccessListener { result ->
                val carreras = result.mapNotNull { it.getString("nombre")?:""}
                carreraList.value = carreras
            }
            .addOnFailureListener { exception ->
                mensajeError.value = "Error al cargar las carreras: ${exception.message}"
            }
    }

    fun validarInformacion(usuario:String,correo: String,password: String, sede: String, carrera: String) {
        when {
            usuario.isEmpty() -> {
                usuarioError.value = "Ingrese un Usuario"
                empRegisterStatus.value = false
            }
            correo.isEmpty() -> {
                correoError.value = "Ingrese su Correo"
                empRegisterStatus.value = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(correo).matches() -> {
                correoError.value = "Correo no válido"
                empRegisterStatus.value = false
            }
            password.isEmpty() -> {
                passwordError.value = "Ingrese una contraseña"
                empRegisterStatus.value = false
            }
            password.length < 6 -> {
                passwordError.value = "Contraseña muy corta, ingrese 6 o más caracteres"
                empRegisterStatus.value = false
            }
            sede.isEmpty() -> {
                mensajeError.value = "Seleccione una sede"
                empRegisterStatus.value = false
            }
            carrera.isEmpty() -> {
                mensajeError.value = "Seleccione una carrera"
                empRegisterStatus.value = false
            }
            else -> registrarUsuario(usuario, correo, password, sede, carrera)
        }
    }

    private fun registrarUsuario(usuario: String,correo: String, password: String, sede: String, carrera : String) {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(correo,password)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    insertarInfoBD(usuario,correo, sede, carrera)
                }else{
                    empRegisterStatus.value = false
                    mensajeError.value = "Error al registrar: ${task.exception?.message}"
                }
            }
    }

    private fun insertarInfoBD(usuario: String, correo: String, sede: String, carrera : String) {
        val db = FirebaseFirestore.getInstance()

        val uidBD = firebaseAuth.uid

        val datosAdministrador = hashMapOf(
            "uid" to uidBD,
            "nombre" to usuario,
            "correo_insti_empl" to correo,
            "sede" to sede,
            "carrera" to carrera,
            "tipoUsuario" to "empleado"
        )

        if (uidBD != null) {
            db.collection("usuarios").document(uidBD).set(datosAdministrador)
                .addOnSuccessListener {
                    empRegisterStatus.value = true
                }
                .addOnFailureListener { e ->
                    empRegisterStatus.value = false
                    mensajeError.value = "Fallo el registro en la Base de Datos debido a ${e.message}"
                }
        } else {
            empRegisterStatus.value = false
            mensajeError.value = "Error al obtener UID del usuario."
        }
    }
}