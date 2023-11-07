package com.example.proyectogym.Views.Admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectogym.R
import com.example.proyectogym.Views.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class PerfilAdminActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_admin)

        val logoutButton = findViewById<Button>(R.id.btnCerrarSesion)
        val user = UserSingleton.getInstance()
        val textUsuario = findViewById<TextView>(R.id.textUsuario)
        val textCorreo = findViewById<TextView>(R.id.textCorreo)


        textUsuario.text = "Administrador"
        textCorreo.text = user.correo

        //Cerrar Sesión
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.selectedItemId = R.id.navigation_perfil
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_zonas -> {
                    startActivity(Intent(this, ZonaActivity::class.java))
                    true
                }
                R.id.navigation_perfil -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}