package com.eventos.eventosapp.view.activity

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.R
import com.eventos.eventosapp.viewmodel.EventoViewModel
import com.eventos.eventosapp.viewmodel.SedeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.Locale

class CrearEventoActivity: AppCompatActivity()  {

    private lateinit var eventoViewModel: EventoViewModel
    private lateinit var sedeViewModel: SedeViewModel

    val pickMediaEvento = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri != null){
            imgEvento.setImageURI(uri)
            ImgUriEvento = uri
        }else{
            Log.i("ari","img no seleccionada")
        }
    }
    val pickMediaProfe = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){ uri->
        if(uri != null){
            imgProfesor.setImageURI(uri)
            ImgUriProfe = uri
        }else{
            Log.i("ari","img no seleccionada")
        }
    }
    lateinit var imgEvento: ImageView
    lateinit var imgProfesor: ImageView
    private var ImgUriEvento: Uri? = null
    private var ImgUriProfe: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_avento)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Creando evento...")
        progressDialog.setCancelable(false)

        eventoViewModel = ViewModelProvider(this)[EventoViewModel::class.java]
        sedeViewModel = ViewModelProvider(this)[SedeViewModel::class.java]

        imgEvento = findViewById(R.id.imgEvento)
        imgProfesor = findViewById(R.id.imgProfesor)
        val btnImgEvento=findViewById<Button>(R.id.btnImgEvento)
        val btnImgProfesor=findViewById<Button>(R.id.btnImgProfesor)
        val btnCrearEvento=findViewById<Button>(R.id.btnCrearEvento)

        val edtNomEvento = findViewById<TextInputEditText>(R.id.edtNomEvento)
        val edtCategoria = findViewById<AutoCompleteTextView>(R.id.edtCategoria)
        val edtSede = findViewById<AutoCompleteTextView>(R.id.edtSede)
        val edtFechaHoraInicio = findViewById<TextInputEditText>(R.id.edtFechaHoraInicio)
        val edtHoraFin = findViewById<TextInputEditText>(R.id.edtHoraFin)
        val edtParticipantes = findViewById<TextInputEditText>(R.id.edtParticipantes)
        val edtDescripcion = findViewById<TextInputEditText>(R.id.edtDescripcion)
        val edtNomProfe = findViewById<TextInputEditText>(R.id.edtNomProfe)
        val edtInfoProfe = findViewById<TextInputEditText>(R.id.edtInfoProfe)
        val myToggle = findViewById<SwitchMaterial>(R.id.myToggle)

        edtFechaHoraInicio.inputType = InputType.TYPE_NULL
        edtHoraFin.inputType = InputType.TYPE_NULL

        edtFechaHoraInicio.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mostrarDateTimePicker(edtFechaHoraInicio)
            }
        }
        edtFechaHoraInicio.setOnClickListener {
            mostrarDateTimePicker(edtFechaHoraInicio)
        }

        edtHoraFin.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                mostrarTimePicker(edtHoraFin)
            }
        }

        edtHoraFin.setOnClickListener {
            mostrarTimePicker(edtHoraFin)
        }

        myToggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Toggle activado
            } else {
                // Toggle desactivado
            }
        }

        btnImgEvento.setOnClickListener{
            pickMediaEvento.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        btnImgProfesor.setOnClickListener{
            pickMediaProfe.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        eventoViewModel.eventoCreado.observe(this) { eventoCreado ->
            if (eventoCreado) {
                progressDialog.dismiss()
                Toast.makeText(this, "Evento creado", Toast.LENGTH_SHORT).show()
            }
        }

        eventoViewModel.errorMessage.observe(this) { errorMessage ->
            errorMessage?.let {
                progressDialog.dismiss()
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        }
        btnCrearEvento.setOnClickListener{
            val maxParticipantes = edtParticipantes.text.toString().toLongOrNull() ?: -1L
            val errorMessage = eventoViewModel.validar(
                edtNomEvento.text.toString(),
                edtCategoria.text.toString(),
                edtSede.text.toString(),
                edtFechaHoraInicio.text.toString(),
                edtHoraFin.text.toString(),
                maxParticipantes,
                edtDescripcion.text.toString(),
                ImgUriEvento,
                ImgUriProfe,
                edtNomProfe.text.toString(),
                edtInfoProfe.text.toString(),
            )
            if (errorMessage != null) {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            } else {
                val fechaHoraString = edtFechaHoraInicio.text.toString()
                val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                val fechaHora = formatter.parse(fechaHoraString)
                val fechayhora = fechaHora?.let { Timestamp(it) }
                progressDialog.show()
                eventoViewModel.crearEvento(
                    edtNomEvento.text.toString(),
                    edtCategoria.text.toString(),
                    edtSede.text.toString(),
                    fechayhora,
                    edtHoraFin.text.toString(),
                    maxParticipantes,
                    edtDescripcion.text.toString(),
                    ImgUriEvento,
                    ImgUriProfe,
                    edtNomProfe.text.toString(),
                    edtInfoProfe.text.toString(),
                    myToggle.isChecked
                )
            }
        }
        val items = listOf("Arte Musical", "Artes Escénicas", "Artes Marciales", "Danzas y Bailes")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, items)
        edtCategoria.setAdapter(adapter)

        sedeViewModel.sedeList.observe(this) { sedes ->
            val adapter = ArrayAdapter(this, R.layout.item_dropdown, sedes)
            edtSede.setAdapter(adapter)
        }
    }

    private fun mostrarDateTimePicker(edt: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = "$selectedHour:$selectedMinute"
                edt.setText("$selectedDate $selectedTime")
            }, hour, minute, true)
            timePickerDialog.show()
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun mostrarTimePicker(edt: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val selectedTime = "$selectedHour:$selectedMinute"
            edt.setText(selectedTime)
        }, hour, minute, true)
        timePickerDialog.show()
    }
}