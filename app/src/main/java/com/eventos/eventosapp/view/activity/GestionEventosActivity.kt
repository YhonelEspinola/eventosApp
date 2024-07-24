package com.eventos.eventosapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.eventos.eventosapp.R
import com.google.firebase.auth.FirebaseAuth

class GestionEventosActivity: AppCompatActivity()  {

    private var  firebaseAuth: FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_eventos)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnCrearEvento = findViewById<Button>(R.id.btnCrearEvento)
        val btnVerEventos = findViewById<Button>(R.id.btnVerEventos)
        val btnCerrar = findViewById<AppCompatButton>(R.id.btnButonCerrarSesion)


        btnCrearEvento.setOnClickListener {
            startActivity(Intent(this, CrearEventoActivity::class.java))
        }
        btnVerEventos.setOnClickListener {
            startActivity(Intent(this, ListaEventoActivity::class.java))
        }
        btnCerrar.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(this, LoginActivity::class.java))
        finishAffinity()
    }

}