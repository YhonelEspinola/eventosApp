package com.eventos.eventosapp.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
import com.google.android.material.textfield.TextInputEditText

class CrearAdminFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_crear_admin, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val btnCrearUsuario : Button = view.findViewById(R.id.btnCrearUsuario)
//        val textNom : TextInputEditText = view.findViewById(R.id.textNomApe)
//        val textCorreo : TextInputEditText = view.findViewById(R.id.textCorreo)
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
    }

    companion object{
        fun crearAdminInstance() : CrearAdminFragment = CrearAdminFragment()
    }
}