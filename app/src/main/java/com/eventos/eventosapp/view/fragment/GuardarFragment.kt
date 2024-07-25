package com.eventos.eventosapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.activity.BuscarEventoActivity

class GuardarFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_guardar,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val iconBuscar = view.findViewById<ImageView>(R.id.icon_buscar)

        iconBuscar.setOnClickListener{
            startActivity(Intent(activity, BuscarEventoActivity::class.java))
        }
    }

    companion object{
        fun newInstance() : GuardarFragment = GuardarFragment()
    }
}