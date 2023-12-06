package com.example.proyectogym.Views.Admin

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.Controllers.ZonaController
import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.R
import android.widget.CheckBox
import com.example.proyectogym.Models.Categoria
import com.google.firebase.storage.FirebaseStorage
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddEjercicioActivity : AppCompatActivity() {

    private val ejercicioController = EjercicioController()
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imagenImageView: ImageView
    private val zonaController = ZonaController()
    private var imagenUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ejercicio)

        val nivelCategoria = intent.extras?.getString("nivelCategoria")
        val nombreCategoria = intent.extras?.getString("nombreCategoria")

        zonaController.obtenerZonas { zonas ->
            zonas?.let {
                val linearLayout = findViewById<LinearLayout>(R.id.linearZonasPrincipales)

                for (zona in zonas) {
                    val checkBox = CheckBox(this)
                    checkBox.text = zona.nombre

                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    layoutParams.marginStart = resources.getDimensionPixelSize(R.dimen.padding_16dp)
                    checkBox.layoutParams = layoutParams
                    linearLayout.addView(checkBox)

                }
            }
        }

        GuardarEjercicio(nivelCategoria, nombreCategoria)
    }

    private fun obtenerChecksMarcados(): List<String> {
        val linearLayout = findViewById<LinearLayout>(R.id.linearZonasPrincipales)
        val checksMarcados = mutableListOf<String>()

        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)

            if (child is CheckBox) {
                if (child.isChecked) {
                    checksMarcados.add(child.text.toString())
                }
            }
        }

        return checksMarcados
    }

    private fun desmarcarChecks() {
        val linearLayout = findViewById<LinearLayout>(R.id.linearZonasPrincipales)

        for (i in 0 until linearLayout.childCount) {
            val child = linearLayout.getChildAt(i)

            if (child is CheckBox) {
                child.isChecked = false
            }
        }
    }

    private fun GuardarEjercicio(nivelCategoria: String?, nombreCategoria: String?) {

        val editTextTitulo = findViewById<EditText>(R.id.tituloEjercicio)
        val editTextCantidad = findViewById<EditText>(R.id.cantidadEjercicio)
        val editTextTiempo = findViewById<EditText>(R.id.tiempoEjercicio)
        val editTextDescripcion = findViewById<EditText>(R.id.textDescripcionEjercicio)
        imagenImageView = findViewById(R.id.GifEjercicio)

        val seleccionarImagenButton = findViewById<Button>(R.id.buttonSeleccionar)
        val guardarEjercicioButton = findViewById<Button>(R.id.buttonGuardarEjercicio)

        seleccionarImagenButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        guardarEjercicioButton.setOnClickListener{

            val zonas = obtenerChecksMarcados()
            val titulo = editTextTitulo.text.toString()
            val cantidad = editTextCantidad.text.toString()
            val tiempo = editTextTiempo.text.toString()
            val descripcion = editTextDescripcion.text.toString()

            if (titulo.isNotEmpty()) {
                val idUnico = UUID.randomUUID().toString()
                val tiempoEntero: Int = tiempo?.toIntOrNull() ?: 0
                val cantidadEntero: Int = cantidad?.toIntOrNull() ?: 0
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val nombreImagen = UUID.randomUUID().toString() + ".gif"
                val pathEnStorage = "ejercicios/$nombreImagen"
                val imagenRef = storageRef.child(pathEnStorage)

                val uploadTask = imagenRef.putFile(imagenUri!!)

                uploadTask.addOnSuccessListener { taskSnapshot ->
                    val nuevoEjercicio = Ejercicio(
                        idUnico,
                        nombreImagen,
                        titulo,
                        cantidadEntero,
                        tiempoEntero,
                        descripcion,
                        nivelCategoria ?: "",
                        nombreCategoria ?: "",
                        zonas
                    )

                    ejercicioController.agregarEjercicio(nuevoEjercicio) { exito ->
                        if (exito) {
                            editTextTitulo.setText("")
                            editTextCantidad.setText("")
                            editTextDescripcion.setText("")
                            editTextTiempo.setText("")
                            desmarcarChecks()
                            
                            Toast.makeText(
                                this,
                                "Ejercicio registrado correctamente",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        this,
                        "Error al registrar el Ejercicio",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else {
                Toast.makeText(this, "El campo de título no puede estar vacío", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            imagenUri = data?.data
            imagenImageView.setImageURI(imagenUri)
        }
    }

}