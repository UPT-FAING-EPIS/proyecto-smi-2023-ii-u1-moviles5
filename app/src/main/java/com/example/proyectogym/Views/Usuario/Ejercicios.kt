package com.example.proyectogym.Views.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.Models.Usuario
import com.example.proyectogym.R
import com.example.proyectogym.Views.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class Ejercicios : AppCompatActivity() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var nivel: String? = null
    private var zona: String? = null
    val ejercicioController = EjercicioController()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ejercicios)

        val user = UserSingleton.getInstance()
        setup(user.correo ?: "")

        ObtenerDatosUsuario()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_usuario)
        bottomNavigationView.selectedItemId = R.id.navigation_home_usuario
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home_usuario -> {
                    startActivity(Intent(this, Ejercicios::class.java))
                    true
                }
                R.id.navigation_ejercicios_usuario -> {
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

    //Obtiene los datos del login
    private fun setup(email: String) {
        title = "Inicio"

        val emailTextView = findViewById<TextView>(R.id.textNombreApellido)
        emailTextView.text = email
    }

    private fun ObtenerDatosUsuario() {
        val email = auth.currentUser?.email

        if (!email.isNullOrBlank()) {
            val database = FirebaseDatabase.getInstance().reference
            val query: Query = database.child("Usuario").orderByChild("correo").equalTo(email)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (data in snapshot.children) {

                            nivel = data.child("nivel").getValue(String::class.java)
                            zona = data.child("zona").getValue(String::class.java)

                            val nivelTextView = findViewById<TextView>(R.id.nivelMisEjercicios)
                            nivelTextView.text = nivel

                            val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewEjercicios)
                            recyclerView.layoutManager = LinearLayoutManager(this@Ejercicios)

                            nivel?.let { nivelNonNull ->
                                zona?.let { zonaNonNull ->
                                    ejercicioController.obtenerEjerciciosPorNivelYZona(nivelNonNull, zonaNonNull) { listaEjercicios ->
                                        if (listaEjercicios != null) {
                                            val adapter = EjercicioAdapter(listaEjercicios)
                                            recyclerView.adapter = adapter
                                            adapter.notifyDataSetChanged()
                                        } else {
                                            Log.e("MainActivity", "Error al obtener la lista de ejercicios")
                                        }
                                    }
                                } ?: run {
                                    Log.e("MainActivity", "Zona es nula")
                                }
                            } ?: run {
                                Log.e("MainActivity", "Nivel es nulo")
                            }

                        }
                    } else {
                        Log.d("UserData", "No se encontraron resultados para el correo electrónico: $email")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DatabaseError", "Error en la consulta: ${error.message}")
                }
            })
        } else {
            Log.d("AuthDetails", "Usuario no autenticado o sin correo electrónico")
        }
    }
}