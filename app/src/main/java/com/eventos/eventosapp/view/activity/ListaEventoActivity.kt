package com.eventos.eventosapp.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.adapter.ListaEventoPequeAdapter
import com.eventos.eventosapp.viewmodel.EventoViewModel

class ListaEventoActivity: AppCompatActivity() {

    private lateinit var viewModel: EventoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_evento)

        viewModel = ViewModelProvider(this)[EventoViewModel::class.java]

        val recycler =findViewById<RecyclerView>(R.id.recyclerEvento)

        val adapterE = ListaEventoPequeAdapter()
        recycler.adapter=adapterE
        recycler.layoutManager= LinearLayoutManager(this)

        viewModel.listarEvento()
        viewModel.listEventos.observe(this) {

            if (it.isNotEmpty()) {
                adapterE.setDatos(it)
            }

        }

    }
}