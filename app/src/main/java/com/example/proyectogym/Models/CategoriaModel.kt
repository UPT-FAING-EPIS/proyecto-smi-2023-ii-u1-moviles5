package com.example.proyectogym.Models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


data class Categoria(
    val nombre: String = "",
    val nivel: String = "",
    val imagen: String = ""
)

class CategoriaModel {
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val categoriaReference: DatabaseReference = firebaseDatabase.reference.child("Categoria")

    fun addCategoria(categoria: Categoria, callback: (Boolean) -> Unit) {
        val newCategoriaReference = categoriaReference.push()
        newCategoriaReference.setValue(categoria)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    fun obtenerCategorias(callback: (List<Categoria>?) -> Unit) {
        categoriaReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categorias = mutableListOf<Categoria>()

                for (categoriaSnapshot in dataSnapshot.children) {
                    val categoria = categoriaSnapshot.getValue(Categoria::class.java)
                    if (categoria != null) {
                        categorias.add(categoria)
                    }
                }
                callback(categorias)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("Error", "Error al obtener datos de Firebase: ${databaseError.message}")
                callback(null)
            }
        })
    }

}