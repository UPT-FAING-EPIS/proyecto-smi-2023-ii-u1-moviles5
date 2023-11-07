package com.example.proyectogym.Controllers

import com.example.proyectogym.Models.Zona
import com.example.proyectogym.Models.ZonaModel

class ZonaController {
    private val zonaModel = ZonaModel()

    fun agregarZona(zona: Zona, callback: (Boolean) -> Unit) {
        zonaModel.addZona(zona, callback)
    }

    fun obtenerZonas(callback: (List<Zona>?) -> Unit) {
        zonaModel.obtenerZonas(callback)
    }
}