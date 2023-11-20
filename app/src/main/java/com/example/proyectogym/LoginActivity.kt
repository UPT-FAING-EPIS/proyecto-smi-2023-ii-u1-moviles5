package com.example.proyectogym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.proyectogym.Models.Usuario
import com.example.proyectogym.Views.Admin.HomeActivity
import com.example.proyectogym.Views.UserSingleton
import com.example.proyectogym.Views.Usuario.NivelActivity
import com.example.proyectogym.Views.Usuario.MainActivity
import com.example.proyectogym.Views.Usuario.RegistroUsuarioActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_main)

        login()
    }

    //Iniciar sesión
    private fun login(){
        title = "Autenticación"
        val loginButton = findViewById<Button>(R.id.loginButton)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)

        val registrate = findViewById<TextView>(R.id.txtRegistrate)

        registrate.setOnClickListener {
            val intent = Intent(this, RegistroUsuarioActivity::class.java)
            startActivity(intent)
        }

        loginButton.setOnClickListener{
            //Si los campos correo y contraseña no estan vacios
            if(emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(emailEditText.text.toString(),
                        passwordEditText.text.toString()).addOnCompleteListener{
                        if(it.isSuccessful){
                            val user = FirebaseAuth.getInstance().currentUser
                            val userEmail = user?.email

                            val database = FirebaseDatabase.getInstance()
                            val usersRef = database.reference.child("Usuario")

                            usersRef.orderByChild("correo").equalTo(userEmail).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    try {
                                        if (dataSnapshot.exists()) {

                                            for (userSnapshot in dataSnapshot.children) {
                                                val userData = userSnapshot.getValue(Usuario::class.java)
                                                if (userData != null) {
                                                    val usuarioLogeado = UserSingleton.getInstance()
                                                    usuarioLogeado.correo = userData.correo
                                                    usuarioLogeado.nombre = userData.nombre
                                                    usuarioLogeado.apellido = userData.apellido
                                                    usuarioLogeado.sexo = userData.sexo

                                                    if(userData.nivel.isEmpty() && userData.zona.isEmpty() && userData.objetivo.isEmpty()){
                                                        val intent = Intent(this@LoginActivity, NivelActivity::class.java)
                                                        startActivity(intent)
                                                    }else{
                                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                                        startActivity(intent)
                                                    }
                                                }
                                            }
                                        } else {
                                            val usuarioLogeado = UserSingleton.getInstance()
                                            usuarioLogeado.nomUsuario = "Administrador"
                                            usuarioLogeado.correo = userEmail ?: ""

                                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                            startActivity(intent)
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(this@LoginActivity, "Error al procesar los datos: ${e.message}", Toast.LENGTH_LONG).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@LoginActivity, "Error al acceder a la base de datos: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }else{
                            showAlert()
                        }
                    }
            }
            else{
                showAlertFieldEmpty()
            }
        }

    }

    //Alerta de error en las credenciales
    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Correo electrónico y/o contraseñas incorrectos")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Alerta para credenciales vacias
    private fun showAlertFieldEmpty(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Los campos correo electrónico y/o contraseñas no pueden estar vacios")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    //Manda datos del usuario a la vista HOME
    private fun showHome(email: String){
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}