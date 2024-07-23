package com.eventos.eventosapp.view.fragment.subfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.adapter.ListaEventoAdapter
import com.eventos.eventosapp.viewmodel.EventoViewModel

class EventoFiltradoSubFragment : Fragment(){

    private lateinit var viewModel: EventoViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.subfragment_evento_filtrado,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[EventoViewModel::class.java]

        val recyclerE = view.findViewById<RecyclerView>(R.id.recyclerEventos)
        val adapterE = ListaEventoAdapter()
        recyclerE.adapter= adapterE
        recyclerE.layoutManager= LinearLayoutManager(context)

        val sede = arguments?.getString("sedeselect","").toString()
        val categoria = arguments?.getString("catego","").toString()

        viewModel.listarEventoPorFiltros(sede,categoria)
        viewModel.listEventos.observe(viewLifecycleOwner) {

            if (it.isNotEmpty()) {
                adapterE.setDatos(it)
            }

        }

    }

    companion object{
        fun newInstance() : EventoFiltradoSubFragment = EventoFiltradoSubFragment()
    }
}