package com.eventos.eventosapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.databinding.ActivityLoginBinding
import com.eventos.eventosapp.viewmodel.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity:AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.btnIniciarSesion.setOnClickListener {
            val email = binding.emailUser.text.toString()
            val password = binding.passwordUser.text.toString()

            viewModel.validarInfo(email,password)
        }

        observerLiveData()
    }

    private fun observerLiveData() {
        viewModel.userLoginStatus.observe(this){ status ->
            if (status){
                comprobarUsuario()
                Toast.makeText(this,"Bienvenido",Toast.LENGTH_SHORT).show()
            }else{
                viewModel.masajeError.value?.let {
                    Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.emailError.observe(this){ errorMessage ->
            binding.emailUser.error = errorMessage
            if (errorMessage != null){
                binding.emailUser.requestFocus()
            }
        }

        viewModel.passwordError.observe(this){ errorMessage ->
            binding.passwordUser.error = errorMessage
            if (errorMessage != null){
                binding.passwordUser.requestFocus()
            }
        }

        viewModel.masajeError.observe(this){ errorMessage ->
            if ( errorMessage != null){
                Toast.makeText(this,errorMessage,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun comprobarUsuario() {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            firestore.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val role = document.getString("tipoUsuario")
                        if (role == "administrador") {
                            // Redirigir a la actividad de administrador
                            startActivity(Intent(this, GestionEventosActivity::class.java))
                            finishAffinity()
                        } else if (role == "empleado") {
                            // Redirigir a la actividad de administrador
                            startActivity(Intent(this, GestionEventosActivity::class.java))
                            finish()
                        }else {
                            // Redirigir a la actividad de estudiante
                            startActivity(Intent(this, MenuActivity::class.java))
                            finish()
                        }
                    } else {
                        Toast.makeText(this, "No se encontró información de usuario.", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error al obtener información de usuario.", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error al obtener información de usuario.", Toast.LENGTH_SHORT).show()
        }
    }
}