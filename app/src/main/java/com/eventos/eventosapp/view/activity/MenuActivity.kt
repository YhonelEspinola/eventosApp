package com.eventos.eventosapp.view.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.eventos.eventosapp.R
import com.eventos.eventosapp.view.fragment.InicioFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuActivity: AppCompatActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.itemInicio -> {
                    val fragment = InicioFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                R.id.itemGuardar -> {
                    val fragment = InicioFragment.newInstance()
                    openFragment(fragment)
                    true
                }

                R.id.itemPerfil -> {
                    val fragment = InicioFragment.newInstance()
                    openFragment(fragment)
                    true
                }
                else -> false
            }
        }
        navView.selectedItemId = R.id.itemInicio
    }

    fun openFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_menu)

        if (currentFragment != null && currentFragment::class.java == fragment::class.java) {
            return // El fragmento ya está mostrado, no hagas nada
        }

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_menu, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}