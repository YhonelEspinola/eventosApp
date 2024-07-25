package com.eventos.eventosapp.view.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.eventos.eventosapp.R
import com.google.firebase.auth.FirebaseAuth

class GestionEventosActivity: AppCompatActivity()  {

    private var  firebaseAuth: FirebaseAuth?=null

    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_eventos)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)

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