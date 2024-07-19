package com.eventos.eventosapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.eventos.eventosapp.R

class GestionEventosActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_eventos)

        val btnCrearEvento = findViewById<Button>(R.id.btnCrearEvento)
        val btnVerEventos = findViewById<Button>(R.id.btnVerEventos)


        btnCrearEvento.setOnClickListener {
            startActivity(Intent(this, CrearEventoActivity::class.java))
        }
        btnVerEventos.setOnClickListener {
            startActivity(Intent(this, ListaEventoActivity::class.java))
        }
    }
}