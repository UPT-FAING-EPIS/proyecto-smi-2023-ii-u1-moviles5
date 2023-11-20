package com.example.proyectogym.Views

class UserSingleton private constructor() {
    var nomUsuario: String = ""
    var correo: String = ""
    var nombre: String = ""
    var apellido: String = ""
    var sexo: String = ""

    /* Datos adicionales */
    var nivel: String = ""
    var objetivo: String = ""
    var zona: String = ""

    companion object {
        @Volatile
        private var instance: UserSingleton? = null

        fun getInstance(): UserSingleton {
            return instance ?: synchronized(this) {
                instance ?: UserSingleton().also { instance = it }
            }
        }
    }
}