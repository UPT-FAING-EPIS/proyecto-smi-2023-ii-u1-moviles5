package com.example.proyectogym

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.proyectogym.Views.Admin.HomeActivity
import com.example.proyectogym.Views.Usuario.RegistroUsuarioActivity
import com.google.firebase.auth.FirebaseAuth

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
            //Toast.makeText(this, "Se hizo clic en el TextView", Toast.LENGTH_SHORT).show()
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
                            showHome(it.result?.user?.email ?: "")
                        }else{
                            showAlert()
                        }
                    }
            }
            //En caso de que esten vacios
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