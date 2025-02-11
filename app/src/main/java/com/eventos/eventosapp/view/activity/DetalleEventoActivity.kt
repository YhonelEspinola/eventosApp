package com.eventos.eventosapp.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.eventos.eventosapp.R
import com.eventos.eventosapp.viewmodel.EventoGuardadosViewModel
import com.eventos.eventosapp.viewmodel.EventoViewModel
import com.eventos.eventosapp.viewmodel.ParticiparViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso


class DetalleEventoActivity: AppCompatActivity(), OnMapReadyCallback {

    private lateinit var eventoGuardadosViewModel: EventoGuardadosViewModel
    private lateinit var participarViewModel : ParticiparViewModel
    private  lateinit var map: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_evento)




        eventoGuardadosViewModel = ViewModelProvider(this)[EventoGuardadosViewModel::class.java]
        participarViewModel = ViewModelProvider(this)[ParticiparViewModel::class.java]

        var isSaved = false
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val guardar= findViewById<ImageView>(R.id.guardar)
        val imagen= findViewById<ImageView>(R.id.imagen)
        val titulo= findViewById<TextView>(R.id.titulo)
        val ubicacion= findViewById<TextView>(R.id.ubicacion)
        val fechaHoraInicio= findViewById<TextView>(R.id.fechaHoraInicio)
        val horaFinal= findViewById<TextView>(R.id.horaFinal)
        val participantesActuales= findViewById<TextView>(R.id.participantesActuales)
        val participantesMax= findViewById<TextView>(R.id.participantesMax)
        val categoria= findViewById<TextView>(R.id.categoria)
        val imgProfesor= findViewById<ImageView>(R.id.imgProfesor)
        val nomProfe= findViewById<TextView>(R.id.nomProfe)
        val infoProfesor= findViewById<TextView>(R.id.infoProfesor)
        val descripcion= findViewById<TextView>(R.id.descripcion)
        val imgmap= findViewById<ImageView>(R.id.imgMap)
        val btnEditar= findViewById<Button>(R.id.btnEditar)
        val BotonesAdmin = findViewById<LinearLayout>(R.id.BotonesAdmin)
        val btnParticipar = findViewById<Button>(R.id.btnParticipar)
        val btnInscritos = findViewById<Button>(R.id.btnVerInscritos)

        val nomevento = intent.getStringExtra("nomevento")
        val categ = intent.getStringExtra("categoria")
        val sede = intent.getStringExtra("sede")
        val fechayhora = intent.getStringExtra("fechayhora")
        val horafin = intent.getStringExtra("horafin")
        val maxparticipantes = intent.getLongExtra("maxparticipantes", 0)
        val actualparticipantes = intent.getLongExtra("actualparticipantes",0)
        val descrip = intent.getStringExtra("descripcion")
        val imgevento = intent.getStringExtra("imgevento")
        val imgprofe = intent.getStringExtra("imgprofe")
        val nomprofe = intent.getStringExtra("nomprofe")
        val infoprofe = intent.getStringExtra("infoprofe")
        val codigo = intent.getStringExtra("codigo")
        val valor = intent.getBooleanExtra("valor", false)
        val estado = intent.getBooleanExtra("estadoevento", true)

        Picasso.get().load(imgevento).into(imagen)
        titulo.text=nomevento
        ubicacion.text=sede
        fechaHoraInicio.text=fechayhora
        horaFinal.text=horafin
        participantesActuales.text= actualparticipantes.toString()
        participantesMax.text=maxparticipantes.toString()
        categoria.text=categ
        Picasso.get().load(imgprofe).into(imgProfesor)
        nomProfe.text=nomprofe
        infoProfesor.text=infoprofe
        descripcion.text=descrip


        imgmap.setOnClickListener{
            mostrarDialogoMap()
        }

        if (valor == true){
            BotonesAdmin.visibility = View.GONE
        }else {
            btnParticipar.visibility = View.GONE
            guardar.visibility= View.GONE
        }

        Log.d("DetalleEventoActivity", "estado: $estado")
        btnEditar.setOnClickListener {
            val intent = Intent(this, EditarEventoActivity::class.java).apply {
                putExtra("nomevento", nomevento)
                putExtra("categoria", categ)
                putExtra("sede", sede)
                putExtra("fechayhora", fechayhora)
                putExtra("horafin", horafin)
                putExtra("maxparticipantes", maxparticipantes)
                putExtra("actualparticipantes", actualparticipantes)
                putExtra("descripcion", descrip)
                putExtra("imgevento", imgevento)
                putExtra("imgprofe", imgprofe)
                putExtra("nomprofe", nomprofe)
                putExtra("infoprofe", infoprofe)
                putExtra("codigo", codigo)
                putExtra("estadoevento", estado)
            }
            startActivity(intent)
        }
        eventoGuardadosViewModel.VerificarEvento(codigo.toString())

        eventoGuardadosViewModel.eventoverificado.observe(this) {
            if (it) {
                isSaved = true
                guardar.setImageResource(R.drawable.icon_guardar_relleno)
            }
        }

        guardar.setOnClickListener{
            progressBar.visibility = View.VISIBLE
            guardar.visibility = View.GONE
            if (isSaved) {
                eventoGuardadosViewModel.QuitarEvento(codigo.toString())
            } else {
                eventoGuardadosViewModel.GuardarEvento(codigo.toString())
            }
            isSaved = !isSaved
        }
        eventoGuardadosViewModel.guardarEventoStatus.observe(this) { success ->
            progressBar.visibility = View.GONE
            guardar.visibility = View.VISIBLE
            if (success) {
                guardar.setImageResource(R.drawable.icon_guardar_relleno)
            } else {
                Toast.makeText(this, "Error al guardar el evento", Toast.LENGTH_SHORT).show()
            }
        }
        eventoGuardadosViewModel.quitarEventoStatus.observe(this) { success ->
            progressBar.visibility = View.GONE
            guardar.visibility = View.VISIBLE
            if (success) {
                guardar.setImageResource(R.drawable.icon_guardar)
            } else {
                Toast.makeText(this, "Error al quitar el evento", Toast.LENGTH_SHORT).show()
            }
        }
        btnInscritos.setOnClickListener{
            val intent = Intent(this, InscritosActivity::class.java).apply {
                putExtra("codigo", codigo)
            }
            startActivity(intent)
        }


        val eventoRef = FirebaseFirestore.getInstance().collection("eventos").document(codigo ?: "")

        eventoRef.addSnapshotListener{ snapshot, e ->
            if(e != null){
                Log.w("DetalleEventoActivity", "Error al escuchar cambios en actualparticipantes", e)
                return@addSnapshotListener
            }

            if(snapshot != null && snapshot.exists()){
                val actualizarParticipantes = snapshot.getLong("actualparticipantes") ?: 0

                participantesActuales.text = actualizarParticipantes.toString()
            }
        }


        btnParticipar.setOnClickListener{
            if (codigo != null) {

                if(participarViewModel.comprobarSesion()){
                    progressBar.visibility = View.GONE
                    participarViewModel.participar(codigo)
                }else{
                    Toast.makeText(this, "Debes iniciar sesión para participar", Toast.LENGTH_SHORT).show()
                }
            }else {
                Toast.makeText(this, "Error al participar", Toast.LENGTH_SHORT).show()
            }

            participarViewModel.participarStatus.observe(this){ success ->
                progressBar.visibility = View.GONE
                if(success){
                    Toast.makeText(this, "Su participacion a sido registrada", Toast.LENGTH_SHORT).show()
                }

            }

            participarViewModel.mensajeError.observe(this){ error ->
                if (error.isNotEmpty()){
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }

            }

        }



    }

    override fun onMapReady(googleMap: GoogleMap) {
        val latitud = intent.getDoubleExtra("latitud", 0.00)
        val longitud = intent.getDoubleExtra("longitud", 0.00)
        val sede = intent.getStringExtra("sede")

        map=googleMap

        val marker= LatLng(latitud,longitud)
        map.addMarker(
            MarkerOptions()
                .position(marker)
                .title(sede)
        )

        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(marker,12f)
        )
    }
    fun mostrarDialogoMap(){
        val dialogView = layoutInflater.inflate(R.layout.dialog_map, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(true)

        val dialog = dialogBuilder.create()
        dialog.show()

        val fragmentMap = supportFragmentManager.findFragmentById(R.id.Map) as SupportMapFragment
        fragmentMap.getMapAsync(this)
        dialog.setOnDismissListener {
            supportFragmentManager.beginTransaction().remove(fragmentMap).commitAllowingStateLoss()
        }
    }
}