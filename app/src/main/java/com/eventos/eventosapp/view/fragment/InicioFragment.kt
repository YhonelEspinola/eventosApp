package com.eventos.eventosapp.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eventos.eventosapp.R
import com.eventos.eventosapp.model.Sede
import com.eventos.eventosapp.view.activity.BuscarEventoActivity
import com.eventos.eventosapp.view.adapter.InicioSedesAdapter
import com.eventos.eventosapp.view.fragment.subfragment.InicioSubFragment
import com.eventos.eventosapp.view.fragment.subfragment.EventoFiltradoSubFragment

class InicioFragment: Fragment(), PopupMenu.OnMenuItemClickListener {
    private lateinit var selectedSede: Sede
    private lateinit var sedeselected: String
    private var selectedCategoria: String = ""
    private lateinit var iconFilter: ImageView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inicio,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerSedes = view.findViewById<RecyclerView>(R.id.recyclerSedes)
        iconFilter = view.findViewById(R.id.icon_filter)
        val iconBuscar = view.findViewById<ImageView>(R.id.icon_buscar)
        val iconNotificacion = view.findViewById<ImageView>(R.id.icon_notificacion)

        val listSede = listOf<Sede>(
            Sede("Inicio"),
            Sede("Arequipa"),
            Sede("Breña"),
            Sede("Callao"),
            Sede("Lima Centro"),
            Sede("Independencia"),
            Sede("San Juan de Lurigancho"),
            Sede("Trujillo"),
        )
        val adapterI = InicioSedesAdapter(listSede,this)
        recyclerSedes.adapter=adapterI
        recyclerSedes.layoutManager=
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)

        if (listSede.isNotEmpty()) {
            onSedeClick(listSede[0]) // Llama al método para cargar el fragmento del primer elemento
        }
        iconBuscar.setOnClickListener{
            startActivity(Intent(activity, BuscarEventoActivity::class.java))
        }

        iconFilter.setOnClickListener{
            showPopup(it)
        }

        iconFilter.setOnLongClickListener {
            val fragment = EventoFiltradoSubFragment()
            val bundle = fragment.arguments ?: Bundle()
            selectedCategoria = ""
            iconFilter.setImageResource(R.drawable.icon_filter)
            ImageViewCompat.setImageTintList(iconFilter, ContextCompat.getColorStateList(requireContext(), R.color.black))
            if(selectedSede.nomsede != "Inicio") {
                bundle.putString("sedeselect", sedeselected)
                bundle.putString("catego", selectedCategoria)

                childFragmentManager.beginTransaction()
                .replace(R.id.subfragment_home, fragment)
                .commit()
            }
            else {
                childFragmentManager.beginTransaction()
                    .replace(R.id.subfragment_home, InicioSubFragment())
                    .commit()
            }

            fragment.arguments = bundle

            true
        }

    }
    fun onSedeClick(sede: Sede) {
        selectedSede = sede
        val fragment:Fragment
        sedeselected = ""
        when (sede.nomsede) {
            "Inicio" ->{fragment = InicioSubFragment()
                iconFilter.setImageResource(R.drawable.icon_filter)
                ImageViewCompat.setImageTintList(iconFilter, ContextCompat.getColorStateList(requireContext(), R.color.black))}

            "Arequipa" -> {fragment = EventoFiltradoSubFragment()
                sedeselected = "Arequipa, Av. Porongoche 500"}

            "Breña"-> {fragment = EventoFiltradoSubFragment()
                sedeselected = "Breña, Av. Brasil 714-792"}

            "Callao"-> {fragment = EventoFiltradoSubFragment()
                sedeselected = "Callao, Av. Óscar R. Benavides 3866- 4070"}

            "Lima Centro"-> {fragment = EventoFiltradoSubFragment()
                sedeselected = "Lima Centro, Av. Uruguay 514"}

            "Independencia"-> {fragment = EventoFiltradoSubFragment()
                sedeselected = "Independencia, Av. Carlos Izaguirre 233"}

            "San Juan de Lurigancho"-> {fragment = EventoFiltradoSubFragment()
                sedeselected = "San Juan de Lurigancho, Av. Próceres de la Independencia 3023"}

            "Trujillo"-> {fragment = EventoFiltradoSubFragment()
                sedeselected = "Trujillo, Borgoño 361"}

            else -> fragment =  InicioSubFragment()
        }
        val bundle = fragment.arguments ?: Bundle()

        if (sede.nomsede != "Inicio"){
            bundle.putString("sedeselect", sedeselected)
            bundle.putString("catego", selectedCategoria)}

        fragment.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.subfragment_home, fragment)
            .commit()
    }
    private fun showPopup(v:View){
        val context = requireContext()
        val popup = PopupMenu(context,v)
        val inflater: MenuInflater =popup.menuInflater
        inflater.inflate(R.menu.menu_emergente,popup.menu)
        popup.setOnMenuItemClickListener ( this )
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod(
                        "setForceShowIcon", Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        val fragment = EventoFiltradoSubFragment()
        val bundle = fragment.arguments ?: Bundle()
        val iconFilter = view?.findViewById<ImageView>(R.id.icon_filter)
        if(selectedSede.nomsede != "Inicio") bundle.putString("sedeselect", sedeselected)
        when (item?.itemId) {
            R.id.itemOpcion1 -> {
                bundle.putString("catego", "Arte Musical")
                selectedCategoria = "Arte Musical"
                iconFilter?.setImageResource(R.drawable.icon_arte_musical)
            }
            R.id.itemOpcion2 -> {
                bundle.putString("catego", "Artes Escénicas")
                selectedCategoria = "Artes Escénicas"
                iconFilter?.setImageResource(R.drawable.icon_artes_escenicas)
            }

            R.id.itemOpcion3 -> {
                bundle.putString("catego", "Artes Marciales")
                selectedCategoria = "Artes Marciales"
                iconFilter?.setImageResource(R.drawable.icon_artes_marciales)
            }

            R.id.itemOpcion4 -> {
                bundle.putString("catego", "Danzas y Bailes")
                selectedCategoria = "Danzas y Bailes"
                iconFilter?.setImageResource(R.drawable.icon_danzas_bailes)
            }
            else -> return false
        }
        iconFilter?.let {
            ImageViewCompat.setImageTintList(it, ContextCompat.getColorStateList(requireContext(), R.color.azul))
        }
        fragment.arguments = bundle

        childFragmentManager.beginTransaction()
            .replace(R.id.subfragment_home, fragment)
            .commit()
        return true
    }

    companion object{
        fun newInstance() : InicioFragment = InicioFragment()
    }
}