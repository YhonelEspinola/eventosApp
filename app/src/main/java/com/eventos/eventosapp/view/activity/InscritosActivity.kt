package com.eventos.eventosapp.view.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.model.Inscrito
import com.eventos.eventosapp.view.adapter.InscritosAdapter
import com.eventos.eventosapp.viewmodel.InscritosViewModel

class InscritosActivity : AppCompatActivity() {

    private lateinit var  inscritosViewModel: InscritosViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inscritos)

        inscritosViewModel = ViewModelProvider(this)[InscritosViewModel::class.java]

        val btnAtras = findViewById<ImageView>(R.id.btnAtras)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewInscritos)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Configurar botón de retroceso
        btnAtras.setOnClickListener {
            finish() // Volver a la pantalla anterior
        }

        // Obtener el id del evento (idevento) desde el intent
        val idevento = intent.getStringExtra("codigo")
        Log.d("InscritosActivity", "idevento recibido: $idevento")

        if (idevento == null) return

        inscritosViewModel.inscritosList.observe(this) { inscritos ->
            Log.d("InscritosActivity", "Lista de inscritos recibida: $inscritos")
            recyclerView.adapter = InscritosAdapter(inscritos) { inscrito ->
                // Mostrar diálogo al hacer clic en un inscrito
                mostrarDialogoPerfil(inscrito)
            }
        }

        // Cargar los inscritos para el evento específico
        inscritosViewModel.cargarInscritos(idevento)
    }

    private fun mostrarDialogoPerfil(inscrito: Inscrito) {
        // Inflar el diseño personalizado del diálogo
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_inscritos_perfil, null)

        // Asignar datos del inscrito a los TextViews en el diálogo
        val textNombre = dialogView.findViewById<TextView>(R.id.textNombre)
        val textCorreo = dialogView.findViewById<TextView>(R.id.textCorreo)
        val textCarrera = dialogView.findViewById<TextView>(R.id.textCarrera)
        val textSede = dialogView.findViewById<TextView>(R.id.textSede)
        val imgCerrar = dialogView.findViewById<ImageView>(R.id.imgCerrar)

        textNombre.text = inscrito.nombre
        textCorreo.text = inscrito.correo_insti_estud
        textCarrera.text = inscrito.carrera
        textSede.text = inscrito.sede

        // Crear el diálogo usando AlertDialog.Builder
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Cerrar el diálogo cuando se presione la X
        imgCerrar.setOnClickListener {
            dialog.dismiss()
        }

        // Mostrar el diálogo
        dialog.show()
    }

    }
