package com.eventos.eventosapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.activity.BuscarEventoActivity
import com.eventos.eventosapp.view.adapter.ListaEventoAdapter
import com.eventos.eventosapp.viewmodel.EventoGuardadosViewModel

class GuardadosFragment: Fragment() {
    private lateinit var eventoGuardadosViewModel: EventoGuardadosViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guardar,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventoGuardadosViewModel = ViewModelProvider(this)[EventoGuardadosViewModel::class.java]
        val iconBuscar = view.findViewById<ImageView>(R.id.icon_buscar)
        val recyclerE = view.findViewById<RecyclerView>(R.id.recyclerEventos)
        val adapterE = ListaEventoAdapter()

        recyclerE.adapter= adapterE
        recyclerE.layoutManager= LinearLayoutManager(context)

        iconBuscar.setOnClickListener{
            startActivity(Intent(activity, BuscarEventoActivity::class.java))
        }

        eventoGuardadosViewModel.listarEvento()
        eventoGuardadosViewModel.listEventos.observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                adapterE.setDatos(it)
            }

        }
    }

    companion object{
        fun newInstance() : GuardadosFragment = GuardadosFragment()
    }
}