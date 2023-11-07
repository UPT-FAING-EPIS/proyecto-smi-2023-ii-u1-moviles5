package com.example.proyectogym.Models

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

data class Ejercicio(
    val imagen: String = "",
    val titulo: String = "",
    val cantidad: String = "",
    val descripcion: String = "",
    val nivel: String = ""

)


class EjercicioModel {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ejercicioReference: DatabaseReference = firebaseDatabase.reference.child("Ejercicio")

    fun addEjercicio(ejercicio: Ejercicio, callback: (Boolean) -> Unit) {
        val newEjercicioReference = ejercicioReference.push()
        newEjercicioReference.setValue(ejercicio)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}