package com.example.proyectogym.Controllers


import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.Models.EjercicioModel

class EjercicioController {
    private val ejercicioModel = EjercicioModel()

    fun agregarEjercicio(ejercicio: Ejercicio, callback: (Boolean) -> Unit) {
        ejercicioModel.addEjercicio(ejercicio, callback)
    }

    fun obtenerListaEjerciciosPorNivel(nivel: String, callback: (List<Ejercicio>?) -> Unit) {
        ejercicioModel.getListaEjerciciosPorNivel(nivel, callback)
    }

    fun obtenerEjercicioPorId(ejercicioId: String, callback: (Ejercicio?) -> Unit) {
        ejercicioModel.getEjercicioPorId(ejercicioId, callback)
    }

    fun obtenerEjerciciosPorNivelYZona(nivel: String, zona: String, callback: (List<Ejercicio>?) -> Unit) {
        ejercicioModel.getEjerciciosPorNivelYZona(nivel, zona, callback)
    }
}