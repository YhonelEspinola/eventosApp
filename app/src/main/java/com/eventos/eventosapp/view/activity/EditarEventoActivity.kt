package com.eventos.eventosapp.view.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.R
import com.eventos.eventosapp.viewmodel.SedeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.google.firebase.Timestamp

class EditarEventoActivity:AppCompatActivity() {

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

    private val storage = Firebase.storage

    private val db = FirebaseFirestore.getInstance()


    lateinit var btnImgEvento: Button
    lateinit var edtNomEvento:TextInputEditText
    lateinit var edtCategoria: AutoCompleteTextView
    lateinit var edtSede: AutoCompleteTextView
    lateinit var edtFechaHoraInicio: TextInputEditText
    lateinit var edtHoraFin: TextInputEditText
    lateinit var edtParticipantes: TextInputEditText
    lateinit var edtDescripcion: TextInputEditText
    lateinit var btnImgProfesor: Button
    lateinit var edtNomProfe: TextInputEditText
    lateinit var edtInfoProfe: TextInputEditText
    lateinit var myToggle:SwitchMaterial
    lateinit var btnEditarEvento: Button
    lateinit var btnCodigo : TextView
    private var fechaHoraInicio: Calendar? = null
    private lateinit var sedeViewModel: SedeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_evento)

        sedeViewModel = ViewModelProvider(this).get(SedeViewModel::class.java)
        btnImgEvento = findViewById(R.id.btnImgEvento)
        imgEvento = findViewById(R.id.imagenEvento)
        edtNomEvento = findViewById(R.id.edtNomEvento)
        edtCategoria = findViewById(R.id.edtCategoria)
        edtSede = findViewById(R.id.edtSede)
        edtFechaHoraInicio = findViewById(R.id.edtFechaHoraInicio)
        edtHoraFin = findViewById(R.id.edtHoraFin)
        edtParticipantes = findViewById(R.id.edtParticipantes)
        edtDescripcion = findViewById(R.id.edtDescripcion)
        imgProfesor = findViewById(R.id.imagenProfesor)
        btnImgProfesor = findViewById(R.id.btnImgProfesor)
        edtNomProfe = findViewById(R.id.edtNomProfe)
        edtInfoProfe = findViewById(R.id.edtInfoProfe)
        myToggle = findViewById(R.id.myToggle)
        btnCodigo = findViewById(R.id.textCodigo)
        btnEditarEvento = findViewById(R.id.btnEditarEvento)

        val items = listOf("Arte Musical", "Artes Escénicas", "Artes Marciales", "Danzas y Bailes")
        val adapter = ArrayAdapter(this, R.layout.item_dropdown, items)
        edtCategoria.setAdapter(adapter)

        sedeViewModel.sedeList.observe(this) { sedes ->
            val adapter = ArrayAdapter(this, R.layout.item_dropdown, sedes)
            edtSede.setAdapter(adapter)
        }
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
        val btnButtonAtras = findViewById<ImageView>(R.id.btnAtras)
        btnButtonAtras.setOnClickListener{
            finish()
        }


        val intent = intent
        var nomevento = intent.getStringExtra("nomevento")
        var categoria = intent.getStringExtra("categoria")
        var sede = intent.getStringExtra("sede")
        var fechayhora = intent.getStringExtra("fechayhora")
        var horafin = intent.getStringExtra("horafin")
        var maxparticipantes = intent.getLongExtra("maxparticipantes", 0)
        var actualparticipantes = intent.getLongExtra("actualparticipantes", 0)
        var descripcion = intent.getStringExtra("descripcion")
        var imgEventoUrl = intent.getStringExtra("imgevento")
        var imgProfeUrl = intent.getStringExtra("imgprofe")
        var nomprofe = intent.getStringExtra("nomprofe")
        var infoprofe = intent.getStringExtra("infoprofe")
        var estadoevento = intent.getBooleanExtra("estadoevento",false)
        val codigo = intent.getStringExtra("codigo")

        edtNomEvento.setText(nomevento)
        edtCategoria.setText(categoria)
        edtSede.setText(sede)
        edtFechaHoraInicio.setText(fechayhora)
        edtHoraFin.setText(horafin)
        edtParticipantes.setText(maxparticipantes.toString())
        edtDescripcion.setText(descripcion)
        Picasso.get().load(imgEventoUrl).into(imgEvento)
        Picasso.get().load(imgProfeUrl).into(imgProfesor)
        edtNomProfe.setText(nomprofe)
        edtInfoProfe.setText(infoprofe)
        myToggle.isChecked = estadoevento
        btnCodigo.text = codigo

        btnEditarEvento.setOnClickListener {
            if (validateFields()) {
                if (ImgUriEvento != null || ImgUriProfe != null) {
                    ImgUriEvento?.let { uriEvento ->
                        uploadImageToFirebaseStorage(uriEvento, "eventos/$codigo/eventImage.jpg") { imgEventoUrl ->
                            ImgUriProfe?.let { uriProfe ->
                                uploadImageToFirebaseStorage(uriProfe, "eventos/$codigo/professorImage.jpg") { imgProfeUrl ->
                                    updateEventInFirestore(imgEventoUrl, imgProfeUrl)
                                }
                            } ?: run {
                                updateEventInFirestore(imgEventoUrl, imgProfeUrl ?: "")
                            }
                        }
                    } ?: run {
                        ImgUriProfe?.let { uriProfe ->
                            uploadImageToFirebaseStorage(uriProfe, "eventos/$codigo/professorImage.jpg") { imgProfeUrl ->
                                updateEventInFirestore(imgEventoUrl ?: "", imgProfeUrl)
                            }
                        } ?: run {
                            updateEventInFirestore(imgEventoUrl ?: "", imgProfeUrl ?: "")
                        }
                    }
                } else {
                    updateEventInFirestore(imgEventoUrl ?: "", imgProfeUrl ?: "")
                }
            } else {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        return when {
            edtNomEvento.text.isNullOrEmpty() -> {
                edtNomEvento.error = "Este campo es obligatorio"
                false
            }
            edtCategoria.text.isNullOrEmpty() -> {
                edtCategoria.error = "Este campo es obligatorio"
                false
            }
            edtSede.text.isNullOrEmpty() -> {
                edtSede.error = "Este campo es obligatorio"
                false
            }
            edtFechaHoraInicio.text.isNullOrEmpty() -> {
                edtFechaHoraInicio.error = "Este campo es obligatorio"
                false
            }
            edtHoraFin.text.isNullOrEmpty() -> {
                edtHoraFin.error = "Este campo es obligatorio"
                false
            }
            edtParticipantes.text.isNullOrEmpty() -> {
                edtParticipantes.error = "Este campo es obligatorio"
                false
            }
            edtDescripcion.text.isNullOrEmpty() -> {
                edtDescripcion.error = "Este campo es obligatorio"
                false
            }
            edtNomProfe.text.isNullOrEmpty() -> {
                edtNomProfe.error = "Este campo es obligatorio"
                false
            }
            edtInfoProfe.text.isNullOrEmpty() -> {
                edtInfoProfe.error = "Este campo es obligatorio"
                false
            }
            else -> true
        }
    }



    private fun mostrarDateTimePicker(edt: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year)
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
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
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            edt.setText(selectedTime)
        }, hour, minute, true)
        timePickerDialog.show()
    }
    fun uploadImageToFirebaseStorage(imageUri: Uri, path: String, onSuccess: (String) -> Unit) {
        val storageRef = storage.reference.child(path)
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener {
            Log.e("FirebaseStorage", "Image upload failed: ${it.message}")
        }
    }

    fun updateEventInFirestore(imgEvento : String, imgProfesor : String) {

        val nomevento = edtNomEvento.text.toString()
        val categoria = edtCategoria.text.toString()
        val sede = edtSede.text.toString()
        val fechaHoraInicioTimestamp = fechaHoraInicio?.let { Timestamp(it.time) }
        val horaFin = edtHoraFin.text.toString()
        val maxParticipantes = edtParticipantes.text.toString().toIntOrNull() ?: 0
        val descripcion = edtDescripcion.text.toString()
        val nomProfe = edtNomProfe.text.toString()
        val infoProfe = edtInfoProfe.text.toString()
        val estadoEvento = myToggle.isChecked
        val codigo = btnCodigo.text.toString()


        val data = HashMap<String, Any>()
        data["nomevento"] = nomevento
        data["categoria"] = categoria
        data["sede"] = sede
        data["fechayhora"] = fechaHoraInicioTimestamp ?: Timestamp.now()
        data["horafin"] = horaFin
        data["maxparticipantes"] = maxParticipantes
        data["descripcion"] = descripcion
        data["imgevento"] = imgEvento
        data["imgprofe"] = imgProfesor
        data["nomprofe"] = nomProfe
        data["infoprofe"] = infoProfe
        data["estadoevento"] = estadoEvento

        db.collection("eventos").document(codigo)
            .update(data)
            .addOnSuccessListener {
                Toast.makeText(this, "Actualizacion exitosa", Toast.LENGTH_LONG).show()
                val intent = Intent(this, ListaEventoActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Algo salió mal", Toast.LENGTH_SHORT).show()
            }
    }

}