package com.example.proyectogym.Controllers

import com.example.proyectogym.Models.Categoria
import com.example.proyectogym.Models.CategoriaModel


class CategoriaController {
    private val categoriaModel = CategoriaModel()

    fun agregarCategoria(categoria: Categoria, callback: (Boolean) -> Unit) {
        categoriaModel.addCategoria(categoria, callback)
    }

    fun obtenerCategorias(callback: (List<Categoria>?) -> Unit) {
        categoriaModel.obtenerCategorias(callback)
    }
}