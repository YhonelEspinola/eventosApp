package com.eventos.eventosapp.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.R
import com.eventos.eventosapp.databinding.ActivityCrearUsuarioBinding
import com.eventos.eventosapp.databinding.FragmentCrearAdminBinding
import com.eventos.eventosapp.view.activity.GestionEventosActivity
import com.eventos.eventosapp.viewmodel.CrearAdminViewModel
import com.google.android.material.textfield.TextInputEditText

class CrearAdminFragment: Fragment() {

    private lateinit var viewModel: CrearAdminViewModel

    private var _binding: FragmentCrearAdminBinding? = null
    private val binding get() = _binding!!
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[CrearAdminViewModel::class.java]

        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Espere profavor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.btnCrearUsuario.setOnClickListener {

            val usuario = binding.textNom.text.toString().trim()
            val correo = binding.textCorreo.text.toString().trim()
            val password = binding.textContraseA.text.toString().trim()

            progressDialog.setMessage("Registrando Informacion...")
            progressDialog.show()

            viewModel.validarInformacion(usuario, correo, password)
        }
        observeLiveData()
    }

    companion object{
        fun crearAdminInstance() : CrearAdminFragment = CrearAdminFragment()
    }

    private fun observeLiveData() {
        viewModel.adminRegisterStatus.observe(viewLifecycleOwner) { status ->
            if (status) {
                progressDialog.dismiss()
                startActivity(Intent(activity, GestionEventosActivity::class.java))
            }else{
                progressDialog.dismiss()
            }
        }

        viewModel.usuarioError.observe(viewLifecycleOwner) { errorMessage ->
            binding.textNom.error = errorMessage
        }

        viewModel.correoError.observe(viewLifecycleOwner) { errorMessage ->
            binding.textCorreo.error = errorMessage
        }

        viewModel.passwordError.observe(viewLifecycleOwner) { errorMessage ->
            binding.textContraseA.error = errorMessage
        }


        viewModel.mensajeError.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                progressDialog.dismiss()
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}