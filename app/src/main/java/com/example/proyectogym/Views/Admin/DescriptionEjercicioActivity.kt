package com.example.proyectogym.Views.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.R
import com.google.firebase.storage.FirebaseStorage

class DescriptionEjercicioActivity : AppCompatActivity() {

    private val ejercicioController = EjercicioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description_ejercicio)
        val ejercicioId = intent.getStringExtra("ejercicioId")

        if (ejercicioId != null) {
            // Obtener el detalle del ejercicio desde Firebase usando el ID
            ejercicioController.obtenerEjercicioPorId(ejercicioId) { ejercicio ->
                Log.e("DetalleEjercicioUsuario22", "Ejercicio: $ejercicio")
                if (ejercicio != null) {
                    cargarVistaDetalle(ejercicio)
                    Log.e("DetalleEjercicioUsuario", "Ejercicio: $ejercicio")
                } else {
                    Log.e("DetalleEjercicioUsuario", "No se encontró el ejercicio con ID: $ejercicioId")
                }
            }
        } else {
            Log.e("DetalleEjercicioUsuario", "El ID del ejercicio es nulo")
        }

    }

    private fun cargarVistaDetalle(ejercicio: Ejercicio) {
        val imgDetalleUsuario = findViewById<ImageView>(R.id.imgDetalle)
        val textTituloDetalleUsuario = findViewById<TextView>(R.id.textTituloDetalle)
        val textSubTituloDetalleUsuario = findViewById<TextView>(R.id.textSubTituloDetalle)
        val textDescripcionDetalleUsuario = findViewById<TextView>(R.id.textDescripcionDetalle)
        val textValoresZonaPrincipal = findViewById<TextView>(R.id.textZonaPrincipal)


        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageReference = storageRef.child("ejercicios/${ejercicio.imagen}")

        val zonas = ejercicio.zonas

        for (zona in zonas) {
            Log.d("Zonas", zona)
        }

        // Cargar la imagen en el ImageView utilizando Glide
        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .into(imgDetalleUsuario)
        }.addOnFailureListener { e ->
            // Manejar errores
        }

        textTituloDetalleUsuario.text = ejercicio.titulo
        if(ejercicio.cantidad > 0){
            textSubTituloDetalleUsuario.text = "x${ejercicio.cantidad}"
        }else{
            val segundosTotales = ejercicio.tiempo
            val minutos = segundosTotales / 60
            val segundos = segundosTotales % 60

            val tiempoFormateado = String.format("%02d:%02d", minutos, segundos)
            textSubTituloDetalleUsuario.text = tiempoFormateado
        }
        textDescripcionDetalleUsuario.text = ejercicio.descripcion

        if (zonas != null && zonas.isNotEmpty()) {
            val zonasText = zonas.joinToString(", ")
            textValoresZonaPrincipal.text = zonasText
        } else {
            textValoresZonaPrincipal.text = "No hay valores de zonas disponibles"
        }
    }
}