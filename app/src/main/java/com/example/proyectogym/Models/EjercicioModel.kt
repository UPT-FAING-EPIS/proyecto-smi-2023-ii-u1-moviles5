package com.example.proyectogym.Models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

data class Ejercicio(
    val id: String = "",
    val imagen: String = "",
    val titulo: String = "",
    val cantidad: Int = 0,
    val tiempo: Int = 0,
    val descripcion: String = "",
    val nivel: String = "",
    val nombreCategoria: String = "",
    val zonas: List<String> = emptyList()
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

    fun getListaEjerciciosPorNivel(nivel: String, callback: (List<Ejercicio>?) -> Unit) {
        ejercicioReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listaEjercicios = mutableListOf<Ejercicio>()
                for (childSnapshot in snapshot.children) {
                    val ejercicio = childSnapshot.getValue(Ejercicio::class.java)
                    ejercicio?.let {
                        if (it.nombreCategoria == nivel) { // Filtrar por nombreCategoria
                            listaEjercicios.add(it)
                        }
                    }
                }
                callback(listaEjercicios)
            }

            override fun onCancelled(error: DatabaseError) {
                // Manejar error de lectura
                callback(null)
            }
        })
    }

    fun getEjercicioPorId(ejercicioId: String, callback: (Ejercicio?) -> Unit) {
        ejercicioReference.orderByChild("id").equalTo(ejercicioId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ejercicios = mutableListOf<Ejercicio>()

                    for (childSnapshot in snapshot.children) {
                        val ejercicio = childSnapshot.getValue(Ejercicio::class.java)
                        ejercicio?.let {
                            ejercicios.add(it)
                        }
                    }
                    callback(ejercicios.firstOrNull())
                }

                override fun onCancelled(error: DatabaseError) {
                    callback(null)
                }
            })
    }

}