package com.eventos.eventosapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eventos.eventosapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            comprobarUsuario()
        }, 1000)
    }
    private fun comprobarUsuario() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId == null) {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        } else {
            FirebaseFirestore.getInstance().collection("usuarios").document(userId).get()
                .addOnSuccessListener {
                    val role = it.getString("tipoUsuario")
                    if (role == "administrador") {
                        // Redirigir a la actividad de administrador
                        startActivity(Intent(this, GestionEventosActivity::class.java))
                        finishAffinity()
                    } else if (role == "empleado"){
                        // Redirigir a la actividad de administrador
                        startActivity(Intent(this, GestionEventosActivity::class.java))
                        finish()
                    } else {
                        // Redirigir a la actividad de estudiante
                        startActivity(Intent(this, MenuActivity::class.java))
                        finish()
                    }
                }
        }
    }

}