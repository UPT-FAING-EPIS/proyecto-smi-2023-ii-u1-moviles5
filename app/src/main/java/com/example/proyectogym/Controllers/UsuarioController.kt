package com.example.proyectogym.Controllers

import com.example.proyectogym.Models.Usuario
import com.example.proyectogym.Models.UsuarioModel

class UsuarioController {
    private val usuarioModel = UsuarioModel()

    fun agregarUsuario(usuario: Usuario, callback: (Boolean) -> Unit) {
        usuarioModel.addUsuario(usuario, callback)
    }
}