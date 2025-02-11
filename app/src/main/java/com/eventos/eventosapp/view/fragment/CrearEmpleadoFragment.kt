package com.eventos.eventosapp.view.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.R
import com.eventos.eventosapp.databinding.FragmentCrearAlumnoBinding
import com.eventos.eventosapp.databinding.FragmentCrearEmpleadoBinding
import com.eventos.eventosapp.view.activity.CrearUsuarioActivity
import com.eventos.eventosapp.view.activity.GestionEventosActivity
import com.eventos.eventosapp.viewmodel.CrearAlumnoViewModel
import com.eventos.eventosapp.viewmodel.CrearEmpleadoViewModel
import com.eventos.eventosapp.viewmodel.SedeViewModel
import com.google.android.material.textfield.TextInputEditText

class CrearEmpleadoFragment: Fragment() {

    private lateinit var viewModel: CrearEmpleadoViewModel

    private var _binding: FragmentCrearEmpleadoBinding? = null
    private val binding get() = _binding!!

    private lateinit var progressDialog: ProgressDialog
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearEmpleadoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel= ViewModelProvider(this)[CrearEmpleadoViewModel::class.java]

        progressDialog = ProgressDialog(context)
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
        viewModel.sedeList.observe(viewLifecycleOwner) { sedes ->
            val adapterSede = ArrayAdapter(requireContext(), R.layout.item_dropdown, sedes)
            binding.edtSede.setAdapter(adapterSede)
        }

        viewModel.carreraList.observe(viewLifecycleOwner) { carreras ->
            val adapterCarrera = ArrayAdapter(requireContext(), R.layout.item_dropdown, carreras)
            binding.edtCarrera.setAdapter(adapterCarrera)
        }

        viewModel.empRegisterStatus.observe(viewLifecycleOwner) { status ->
            if (status) {
                progressDialog.dismiss()
                binding.textNomApe.text?.clear()
                binding.textCorreo.text?.clear()
                binding.edtSede.text?.clear()
                binding.edtCarrera.text?.clear()
                binding.textContraseA.text?.clear()
            }else{
                progressDialog.dismiss()
            }
        }

        viewModel.usuarioError.observe(viewLifecycleOwner) { errorMessage ->
            binding.textNomApe.error = errorMessage
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

    companion object{
        fun crearEmpleadoInstance() : CrearEmpleadoFragment = CrearEmpleadoFragment()
    }
}