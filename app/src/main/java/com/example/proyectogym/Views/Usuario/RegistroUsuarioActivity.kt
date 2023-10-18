package com.example.proyectogym.Views.Usuario

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.proyectogym.Controllers.UsuarioController
import com.example.proyectogym.Models.Usuario
import com.example.proyectogym.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistroUsuarioActivity : AppCompatActivity() {

    private val usuarioController = UsuarioController()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        GuardarUsuario()
    }

    private fun GuardarUsuario(){
        val editTextCorreoUsuario = findViewById<EditText>(R.id.editTextCorreoUsuario)
        val editTextNombreUsuario = findViewById<EditText>(R.id.editTextNombreUsuario)
        val editTextApellidoUsuario = findViewById<EditText>(R.id.editTextApellidoUsuario)
        val editTextContraseniaUsuario = findViewById<EditText>(R.id.editTextContraseniaUsuario)
        val rboMasculinoUsuario = findViewById<RadioButton>(R.id.rboMasculinoPersonal)
        val rboFemeninoUsuario = findViewById<RadioButton>(R.id.rboFemeninoPersonal)
        val guardarUsuarioButton = findViewById<Button>(R.id.guardarUsuario)
        val rboGrupoUsuario = findViewById<RadioGroup>(R.id.rboGrupoUsuario)

        val auth = Firebase.auth

        guardarUsuarioButton.setOnClickListener{
            val correo = editTextCorreoUsuario.text.toString()
            val nombre = editTextNombreUsuario.text.toString()
            val apellido = editTextApellidoUsuario.text.toString()
            val contrasenia = editTextContraseniaUsuario.text.toString()
            var sexo: String = ""

            if (rboMasculinoUsuario.isChecked){
                sexo = rboMasculinoUsuario.text.toString()
            }else{
                sexo = rboFemeninoUsuario.text.toString()
            }

            if (correo.isNotEmpty() && nombre.isNotEmpty() && apellido.isNotEmpty() && contrasenia.isNotEmpty() && sexo.isNotEmpty()) {
                // Verificar si el correo ya existe en Firebase Authentication
                auth.fetchSignInMethodsForEmail(correo)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val signInMethods = task.result?.signInMethods
                            if (signInMethods.isNullOrEmpty()) {
                                // El correo no está asociado a ninguna cuenta, se puede crear un nuevo usuario
                                auth.createUserWithEmailAndPassword(correo, contrasenia)
                                    .addOnCompleteListener { createTask ->
                                        if (createTask.isSuccessful) {
                                            // Guardar los datos del personal en Firebase Realtime Database
                                            val nuevoPersonal = Usuario(
                                                correo, nombre, apellido, contrasenia, sexo, 1
                                            )
                                            usuarioController.agregarUsuario(nuevoPersonal) { exito ->
                                                if (exito) {

                                                    editTextCorreoUsuario.setText("")
                                                    editTextNombreUsuario.setText("")
                                                    editTextApellidoUsuario.setText("")
                                                    editTextCorreoUsuario.setText("")
                                                    editTextContraseniaUsuario.setText("")
                                                    rboGrupoUsuario.clearCheck()

                                                    Toast.makeText(
                                                        this,
                                                        "Usuario registrado correctamente",
                                                        Toast.LENGTH_SHORT
                                                    ).show()

                                                } else {
                                                    Toast.makeText(
                                                        this,
                                                        "Error al registrar el Usuario",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "El correo ya está registrado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                            } else {
                                // El correo ya está asociado a una cuenta existente
                                Toast.makeText(this, "El correo ya está registrado", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Error al verificar el correo
                            Toast.makeText(this, "Error al verificar el correo", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}