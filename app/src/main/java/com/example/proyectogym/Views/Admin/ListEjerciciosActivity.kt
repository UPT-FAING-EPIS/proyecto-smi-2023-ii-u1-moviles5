package com.example.proyectogym.Views.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.proyectogym.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class ListEjerciciosActivity : AppCompatActivity() {

    private lateinit var addButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_ejercicios)

        addButton = findViewById(R.id.buttonAddEjercicio)

        val imagenUrl = intent.extras?.getString("imagenUrl")
        val textoCategoria = intent.extras?.getString("textoCategoria")

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
            startActivity(intent)
        }
    }
}