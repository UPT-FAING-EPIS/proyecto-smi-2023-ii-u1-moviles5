package com.example.proyectogym.Views.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.R
import com.example.proyectogym.Views.Usuario.DetalleEjercicioUsuarioActivity
import com.example.proyectogym.Views.Usuario.EjercicioAdapter
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ListEjerciciosActivity : AppCompatActivity() {

    private lateinit var addButton: Button
    val ejercicioController = EjercicioController()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ejercicios)

        addButton = findViewById(R.id.buttonAddEjercicio)

        val imagenUrl = intent.extras?.getString("imagenUrl")
        val textoCategoria = intent.extras?.getString("textoCategoria")
        val nivelCategoria = intent.extras?.getString("nivelCategoria")

        val imageView = findViewById<ImageView>(R.id.backgroundCategoria)
        val textView = findViewById<TextView>(R.id.textTituloCategoria)
        textView.text = textoCategoria

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageReference = storageRef.child("categoria/$imagenUrl")

        // Cargar la imagen en el ImageView utilizando Picasso
        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(imageView)
        }.addOnFailureListener { e ->
            // Manejar errores
        }

        addButton.setOnClickListener {
            val intent = Intent(this, AddEjercicioActivity::class.java)
            intent.putExtra("nivelCategoria", nivelCategoria)
            intent.putExtra("nombreCategoria", textoCategoria)
            startActivity(intent)
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
                            val intent = Intent(this@ListEjerciciosActivity, DescriptionEjercicioActivity::class.java)
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