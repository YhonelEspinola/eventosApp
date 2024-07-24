package com.eventos.eventosapp.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.fragment.CrearAdminFragment
import com.eventos.eventosapp.view.fragment.CrearAlumnoFragment
import com.eventos.eventosapp.view.fragment.CrearEmpleadoFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class CrearUsuarioActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_usuario)

        val nav_view =findViewById<BottomNavigationView>(R.id.nav_view)
        nav_view.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itemAdmin -> {
                    val fragment =CrearAdminFragment.crearAdminInstance()
                    openFregment(fragment)
                    true
                }
                R.id.itemEmpleado -> {
                    val fragment = CrearEmpleadoFragment.crearEmpleadoInstance()
                    openFregment(fragment)
                    true
                }
                R.id.itemAlumno -> {
                    val fragment = CrearAlumnoFragment.crearAlumnoInstance()
                    openFregment(fragment)
                    true
                }
                else -> false
            }
        }

        nav_view.selectedItemId = R.id.itemAdmin
    }

    fun openFregment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_menu, fragment)
        transaction.commit()
    }
}