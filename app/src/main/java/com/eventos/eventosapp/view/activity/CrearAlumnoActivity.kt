package com.eventos.eventosapp.view.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.R
import com.eventos.eventosapp.databinding.ActivityCrearAlumnoBinding
import com.eventos.eventosapp.viewmodel.CrearAlumnoViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CrearAlumnoActivity:AppCompatActivity() {

    private lateinit var viewModel: CrearAlumnoViewModel

    private lateinit var binding: ActivityCrearAlumnoBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearAlumnoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel= ViewModelProvider(this)[CrearAlumnoViewModel::class.java]

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere profavor")
        progressDialog.setCanceledOnTouchOutside(false)


        binding.btnCrearUsuario.setOnClickListener {
            val usuario = binding.textNomApe.text.toString().trim()
            val correo = binding.textCorreo.text.toString().trim()
            val sede = binding.edtSede.text.toString().trim()
            val carrea = binding.edtCarrera.text.toString().trim()
            val password = binding.textContraseA.text.toString().trim()

            progressDialog.setMessage("Registrando Informacion...")
            progressDialog.show()

            viewModel.validarInformacion(usuario,correo,password,sede,carrea)
        }

        observeLiveData()

    }

    private fun observeLiveData() {

        viewModel.sedeList.observe(this) { sedes ->
            val adapterSede = ArrayAdapter(this, R.layout.item_dropdown, sedes)
            binding.edtSede.setAdapter(adapterSede)
        }

        viewModel.carreraList.observe(this) { carreras ->
            val adapterCarrera = ArrayAdapter(this, R.layout.item_dropdown, carreras)
            binding.edtCarrera.setAdapter(adapterCarrera)
        }

        viewModel.alumRegisterStatus.observe(this) { status ->
            if (status) {
                progressDialog.dismiss()
                startActivity(Intent(this, GestionEventosActivity::class.java))
                finish()
            }else{
                progressDialog.dismiss()
            }
        }

        viewModel.usuarioError.observe(this) { errorMessage ->
            binding.textNomApe.error = errorMessage
        }

        viewModel.correoError.observe(this) { errorMessage ->
            binding.textCorreo.error = errorMessage
        }

        viewModel.passwordError.observe(this) { errorMessage ->
            binding.textContraseA.error = errorMessage
        }

        viewModel.mensajeError.observe(this) { errorMessage ->
            if (errorMessage != null) {
                progressDialog.dismiss()
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}