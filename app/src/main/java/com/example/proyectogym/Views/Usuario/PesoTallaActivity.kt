package com.example.proyectogym.Views.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.proyectogym.R
import com.example.proyectogym.Views.UserSingleton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PesoTallaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peso_talla)

        val txtPeso = findViewById<EditText>(R.id.txtPeso)
        val txtAltura = findViewById<EditText>(R.id.txtAltura)
        val buttonSiguiente = findViewById<Button>(R.id.buttonSiguiente4)

        buttonSiguiente.setOnClickListener {


            val peso = txtPeso.text.toString()
            val altura = txtAltura.text.toString()

            if (peso.isNotEmpty() && altura.isNotEmpty()) {

                val usuarioLogeado = UserSingleton.getInstance()

                val database = FirebaseDatabase.getInstance()
                val usuariosRef = database.getReference("Usuario")

                usuariosRef.orderByChild("correo").equalTo(usuarioLogeado.correo)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (childSnapshot in snapshot.children) {
                                // Obtiene la clave del nodo (ID del usuario)
                                val userId = childSnapshot.key

                                // Actualiza los datos del usuario
                                usuariosRef.child(userId!!)
                                    .updateChildren(
                                        mapOf(
                                            "peso" to peso,
                                            "talla" to altura,
                                            "nivel" to usuarioLogeado.nivel,
                                            "objetivo" to usuarioLogeado.objetivo,
                                            "zona" to usuarioLogeado.zona
                                        )
                                    )
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            this@PesoTallaActivity,
                                            "Actualización exitosa",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent = Intent(
                                            this@PesoTallaActivity,
                                            MainActivity::class.java
                                        )
                                        startActivity(intent)
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            this@PesoTallaActivity,
                                            "Error al actualizar: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@PesoTallaActivity,
                                "Error al buscar el usuario: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}