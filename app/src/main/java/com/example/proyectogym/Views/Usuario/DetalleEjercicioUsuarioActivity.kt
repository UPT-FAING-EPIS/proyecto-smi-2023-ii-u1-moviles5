package com.example.proyectogym.Views.Usuario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.bumptech.glide.Glide
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class DetalleEjercicioUsuarioActivity : AppCompatActivity() {

    private val ejercicioController = EjercicioController()
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var chronometer: Chronometer

    interface OnDataLoadedListener {
        fun onDataLoaded()
        fun onDataLoadError()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_ejercicio_usuario)

        val ejercicioId = intent.getStringExtra("ejercicioId")

        if (ejercicioId != null) {
            ejercicioController.obtenerEjercicioPorId(ejercicioId) { ejercicio ->
                Log.e("Ejercicios", "EjercicioXD: $ejercicio")
                if (ejercicio != null) {
                    cargarVistaDetalle(ejercicio, object : OnDataLoadedListener {
                        override fun onDataLoaded() {
                            // Todas las operaciones asíncronas han finalizado, mostrar la vista
                            Log.e("DetalleEjercicioUsuario", "Ejercicio cargado con éxito")
                        }

                        override fun onDataLoadError() {
                            Log.e("DetalleEjercicioUsuario", "Error al cargar el ejercicio")
                        }
                    })
                } else {
                    Log.e("DetalleEjercicioUsuario", "No se encontró el ejercicio con ID: $ejercicioId")
                }
            }
        } else {
            Log.e("DetalleEjercicioUsuario", "El ID del ejercicio es nulo")
        }

        val btnIniciarEjercicio = findViewById<Button>(R.id.btnIniciarEjercicio)
        btnIniciarEjercicio.setOnClickListener {
            mostrarModal()
        }

    }

    private fun cargarVistaDetalle(ejercicio: Ejercicio, onDataLoadedListener: OnDataLoadedListener) {
        val imgDetalleUsuario = findViewById<ImageView>(R.id.imgDetalleUsuario)
        val textTituloDetalleUsuario = findViewById<TextView>(R.id.textTituloDetalleUsuario)
        val textSubTituloDetalleUsuario = findViewById<TextView>(R.id.textSubTituloDetalleUsuario)
        val textDescripcionDetalleUsuario = findViewById<TextView>(R.id.textDescripcionDetalleUsuario)
        val textValoresZonaPrincipal = findViewById<TextView>(R.id.textValoresZonaPrincipal)

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageReference = storageRef.child("ejercicios/${ejercicio.imagen}")
        val zonas = ejercicio.zonas

        // Cargar la imagen en el ImageView utilizando Glide
        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(this)
                .load(uri)
                .into(imgDetalleUsuario)

            onDataLoadedListener.onDataLoaded()
        }.addOnFailureListener { e ->
            onDataLoadedListener.onDataLoadError()
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

    private fun mostrarModal() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val view = inflater.inflate(R.layout.modal_layout, null)
        builder.setView(view)

        val contadorTextView = view.findViewById<TextView>(R.id.contadorTextView)
        chronometer = view.findViewById(R.id.cronometro)

        countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                contadorTextView.text = seconds.toString()
            }

            override fun onFinish() {
                contadorTextView.visibility = View.GONE
                chronometer.base = SystemClock.elapsedRealtime()
                chronometer.start()
            }
        }

        val dialog = builder.create()
        dialog.show()

        val btnFinalizar = view.findViewById<Button>(R.id.btnFinalizar)

        btnFinalizar.setOnClickListener {
            chronometer.stop()
            dialog.dismiss()
        }

        countDownTimer.start()
    }

}