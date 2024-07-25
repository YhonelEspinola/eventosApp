package com.eventos.eventosapp.view.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.activity.BuscarEventoActivity
import com.eventos.eventosapp.view.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class PerfilFragment: Fragment() {

    private var  firebaseAuth: FirebaseAuth?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        val iconBuscar = view.findViewById<ImageView>(R.id.icon_buscar)
        val cerarSesion : AppCompatButton = view.findViewById(R.id.cerra_sesion)

        iconBuscar.setOnClickListener{
            startActivity(Intent(activity, BuscarEventoActivity::class.java))
        }

        cerarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        clearPreferences()
        startActivity(Intent(activity,LoginActivity::class.java))
        activity?.finish()
    }

    private fun clearPreferences() {
        val sharedPreferences = requireActivity().getSharedPreferences("loginPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.remove("remember")
        editor.apply()
    }

    companion object{
        fun newInstance() : PerfilFragment = PerfilFragment()
    }
}