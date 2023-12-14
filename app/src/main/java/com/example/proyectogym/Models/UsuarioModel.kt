package com.example.proyectogym.Models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Usuario(
    val correo: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val contrasenia: String = "",
    val sexo: String = "",
    val tipoUsuario: Int = 0,
    val peso: String = "",
    val talla: String = "",
    val zona: String = "",
    val objetivo: String = "",
    val nivel: String = ""
)


class UsuarioModel {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val usuarioReference: DatabaseReference = firebaseDatabase.reference.child("Usuario")

    fun addUsuario(personal: Usuario, callback: (Boolean) -> Unit) {
        val newUsuarioReference = usuarioReference.push()
        newUsuarioReference.setValue(personal)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}