package com.example.proyectogym.Views.Admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.example.proyectogym.Controllers.EjercicioController
import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.R

class AddEjercicioActivity : AppCompatActivity() {

    private val ejercicioController = EjercicioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ejercicio)

        GuardarEjercicio()
    }

    private fun GuardarEjercicio(){

        val editTextTitulo = findViewById<EditText>(R.id.tituloEjercicio)
        val editTextCantidad = findViewById<EditText>(R.id.cantidadEjercicio)
        val editTextDescripcion = findViewById<EditText>(R.id.textDescripcionEjercicio)
        val nivel = findViewById<Spinner>(R.id.cboNivelEjercicio)

        val guardarEjercicioButton = findViewById<Button>(R.id.buttonGuardarEjercicio)

        guardarEjercicioButton.setOnClickListener{

            val titulo = editTextTitulo.text.toString()
            val cantidad = editTextCantidad.text.toString()
            val descripcion = editTextDescripcion.text.toString()
            val nivel = nivel.selectedItem.toString()

            val nuevoEjercicio = Ejercicio(
                titulo, cantidad, descripcion, nivel
            )

            ejercicioController.agregarEjercicio(nuevoEjercicio) { exito ->
                if (exito) {

                    editTextTitulo.setText("")
                    editTextCantidad.setText("")
                    editTextDescripcion.setText("")

                    Toast.makeText(
                        this,
                        "Ejercicio registrado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this,
                        "Error al registrar el Ejercicio",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

    }



}