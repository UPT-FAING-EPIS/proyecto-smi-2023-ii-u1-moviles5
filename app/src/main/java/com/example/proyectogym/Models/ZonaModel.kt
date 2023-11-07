package com.example.proyectogym.Models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Zona(
    val nombre: String = "",
    val estado: String = ""
)

class ZonaModel {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val zonaReference: DatabaseReference = firebaseDatabase.reference.child("Zona")

    fun addZona(zona: Zona, callback: (Boolean) -> Unit) {
        val newCategoriaReference = zonaReference.push()
        newCategoriaReference.setValue(zona)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun obtenerZonas(callback: (List<Zona>?) -> Unit) {
        zonaReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val zonas = mutableListOf<Zona>()

                for (categoriaSnapshot in dataSnapshot.children) {
                    val zona = categoriaSnapshot.getValue(Zona::class.java)
                    if (zona != null) {
                        zonas.add(zona)
                    }
                }
                callback(zonas)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "Error al obtener datos de Firebase: ${databaseError.message}")
                callback(null)
            }
        })
    }
}