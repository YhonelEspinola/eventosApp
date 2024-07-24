package com.eventos.eventosapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
import com.eventos.eventosapp.viewmodel.SedeViewModel
import com.google.android.material.textfield.TextInputEditText

class CrearEmpleadoFragment: Fragment() {

    private lateinit var sedeViewModel: SedeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear_empleado, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val btnCrearUsuario : Button = view.findViewById(R.id.btnCrearUsuario)
//        val textNom : TextInputEditText = view.findViewById(R.id.textNomApe)
//        val textCorreo : TextInputEditText = view.findViewById(R.id.textCorreo)
//        val edtSede : AutoCompleteTextView= view.findViewById(R.id.edtSede)
//        val edtCarrera : AutoCompleteTextView= view.findViewById(R.id.edtCarrera)
//        val textContraseña : TextInputEditText = view.findViewById(R.id.textContraseña)
//
//        btnCrearUsuario.setOnClickListener {
//
//            val Nom = textNom.text.toString()
//            val Correo = textCorreo.text.toString()
//            val Contraseña = textContraseña.text.toString()
//
//            val Null = Nom + "" + Correo + Contraseña
//        }

//        val items = listOf("Arte Musical", "Artes Escénicas", "Artes Marciales", "Danzas y Bailes")
//        val adapter = context?.let { ArrayAdapter(it, R.layout.item_dropdown, items) }
//        edtCarrera.setAdapter(adapter)
//
//        sedeViewModel.sedeList.observe(viewLifecycleOwner) { sedes ->
//            val adapter = context?.let { ArrayAdapter(it, R.layout.item_dropdown, sedes) }
//            edtSede.setAdapter(adapter)
//        }
    }

    companion object{
        fun crearEmpleadoInstance() : CrearEmpleadoFragment = CrearEmpleadoFragment()
    }
}