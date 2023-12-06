package com.example.proyectogym.Views.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.proyectogym.LoginActivity
import com.example.proyectogym.R
import com.example.proyectogym.Views.Admin.HomeActivity
import com.example.proyectogym.Views.Admin.PerfilAdminActivity
import com.example.proyectogym.Views.Admin.ZonaActivity
import com.example.proyectogym.Views.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class PerfilUsuarioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        val logoutButton = findViewById<Button>(R.id.btnCerrarSesion)
        val user = UserSingleton.getInstance()
        val textUsuario = findViewById<TextView>(R.id.textUsuario)
        val textCorreo = findViewById<TextView>(R.id.textCorreo)


        textUsuario.text = user.nombre + " " + user.apellido
        textCorreo.text = user.correo

        //Cerrar Sesión
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@PerfilUsuarioActivity, LoginActivity::class.java)
            startActivity(intent)

            finish()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_usuario)
        bottomNavigationView.selectedItemId = R.id.navigation_perfil_usuario
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home_usuario -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_informe_usuario -> {
                    startActivity(Intent(this, InformesActivity::class.java))
                    true
                }
                R.id.navigation_perfil_usuario -> {
                    startActivity(Intent(this, PerfilUsuarioActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}