package com.eventos.eventosapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.eventos.eventosapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GestionEventosActivity: AppCompatActivity()  {

    private var  firebaseAuth: FirebaseAuth?=null
    private lateinit var firestore : FirebaseFirestore

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_eventos)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

        val btnCrearEvento = findViewById<Button>(R.id.btnCrearEvento)
        val btnVerEventos = findViewById<Button>(R.id.btnVerEventos)
        val btnCrearUser = findViewById<Button>(R.id.btnNewUser)
        val btnCerrar = findViewById<AppCompatButton>(R.id.btnButonCerrarSesion)


        btnCrearEvento.setOnClickListener {
            startActivity(Intent(this, CrearEventoActivity::class.java))
        }
        btnVerEventos.setOnClickListener {
            startActivity(Intent(this, ListaEventoActivity::class.java))
        }
        btnCrearUser.setOnClickListener {
            comprobarUser()
        }
        btnCerrar.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun comprobarUser() {
        val userId = firebaseAuth?.currentUser?.uid
        if (userId != null) {
            firestore.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("tipoUsuario")
                        if (role == "administrador") {
                            startActivity(Intent(this, CrearUsuarioActivity::class.java))
                        } else if (role == "empleado") {
                            startActivity(Intent(this, CrearAlumnoActivity::class.java))
                        }
                    } else {
                        Toast.makeText(this, "No se encontró información de usuario.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al obtener información de usuario.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error al obtener información de usuario.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        clearPreferences()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

    private fun clearPreferences() {
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.remove("remember")
        editor.apply()
    }

}