package com.eventos.eventosapp.view.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.eventos.eventosapp.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.Picasso

class DetalleEventoActivity: AppCompatActivity(), OnMapReadyCallback {

    private  lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_evento)

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