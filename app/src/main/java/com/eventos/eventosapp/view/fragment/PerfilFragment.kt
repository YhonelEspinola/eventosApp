package com.eventos.eventosapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
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

        val cerarSesion : AppCompatButton = view.findViewById(R.id.cerra_sesion)

        cerarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    private fun cerrarSesion(){
        firebaseAuth!!.signOut()
        startActivity(Intent(activity,LoginActivity::class.java))
        activity?.finish()
    }
    companion object{
        fun newInstance() : PerfilFragment = PerfilFragment()
    }
}