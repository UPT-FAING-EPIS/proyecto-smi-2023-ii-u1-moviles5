package com.example.proyectogym.Controllers


import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.Models.EjercicioModel

class EjercicioController {
    private val ejercicioModel = EjercicioModel()

    fun agregarEjercicio(ejercicio: Ejercicio, callback: (Boolean) -> Unit) {
        ejercicioModel.addEjercicio(ejercicio, callback)
    }
}