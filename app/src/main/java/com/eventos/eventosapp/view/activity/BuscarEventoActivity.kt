package com.eventos.eventosapp.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.adapter.ListaEventoAdapter
import com.eventos.eventosapp.viewmodel.EventoViewModel

class BuscarEventoActivity: AppCompatActivity() {

    private lateinit var viewModel: EventoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_buscar_evento)

        viewModel = ViewModelProvider(this)[EventoViewModel::class.java]

        val recycler =findViewById<RecyclerView>(R.id.recyclerSearch)
        val searchView = findViewById<SearchView>(R.id.search)

        val adapterE = ListaEventoAdapter()
        recycler.adapter=adapterE
        recycler.layoutManager= LinearLayoutManager(this)

        viewModel.listarEvento()
        viewModel.listEventos.observe(this) {

            if (it.isNotEmpty()) {
                adapterE.setDatos(it)
            }

        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { adapterE.filtrarDatos(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { adapterE.filtrarDatos(it) }
                return false
            }
        })

    }
}