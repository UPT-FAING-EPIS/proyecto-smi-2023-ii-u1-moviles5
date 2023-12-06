package com.example.proyectogym.Views.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ListEjerciciosUsuarioActivity : AppCompatActivity() {

    val ejercicioController = EjercicioController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ejercicios_usuario)

        val imagenUrl = intent.extras?.getString("imagenUrl")
        val textoCategoria = intent.extras?.getString("textoCategoria")

        val imageView = findViewById<ImageView>(R.id.backgroundCategoriaUsuario)
        val textView = findViewById<TextView>(R.id.textTituloCategoriaUsuario)
        textView.text = textoCategoria

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageReference = storageRef.child("categoria/$imagenUrl")

        // Cargar la imagen en el ImageView utilizando Glide
        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .into(imageView)
        }.addOnFailureListener { e ->
            // Manejar errores
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewEjercicios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val nivelDeseado = textoCategoria

        if (nivelDeseado.isNullOrEmpty()) {
            Log.e("MainActivity", "El nivel es nulo o vacío")
        } else {
            ejercicioController.obtenerListaEjerciciosPorNivel(nivelDeseado) { listaEjercicios ->
                if (listaEjercicios != null) {
                    val adapter = EjercicioAdapter(listaEjercicios)
                    recyclerView.adapter = adapter

                    // Configurar el OnItemClickListener
                    adapter.setOnItemClickListener(object : EjercicioAdapter.OnItemClickListener {
                        override fun onItemClick(id: String) {
                            // Manejar el clic del elemento aquí, por ejemplo, iniciar la nueva actividad y pasar el ID
                            val intent = Intent(this@ListEjerciciosUsuarioActivity, DetalleEjercicioUsuarioActivity::class.java)
                            intent.putExtra("ejercicioId", id)
                            startActivity(intent)
                        }
                    })
                    adapter.notifyDataSetChanged()
                } else {
                    Log.e("MainActivity", "Error al obtener la lista de ejercicios")
                }
            }
        }

    }
}