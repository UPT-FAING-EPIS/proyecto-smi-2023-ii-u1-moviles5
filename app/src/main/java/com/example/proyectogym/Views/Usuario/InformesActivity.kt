package com.example.proyectogym.Views.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.example.proyectogym.LoginActivity
import com.example.proyectogym.R
import com.example.proyectogym.Views.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class InformesActivity : AppCompatActivity() {

    private lateinit var pesoEditText: EditText
    private lateinit var alturaEditText: EditText
    private lateinit var resultadoTextView: TextView
    private lateinit var progressBarIMC: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_informes)


        pesoEditText = findViewById(R.id.editTextPeso)
        alturaEditText = findViewById(R.id.editTextAltura)
        resultadoTextView = findViewById(R.id.textViewResultado)
        progressBarIMC = findViewById(R.id.progressBarIMC)

        val calcularButton: Button = findViewById(R.id.btnCalcular)
        calcularButton.setOnClickListener {
            calcularIMC()
        }




        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_usuario)
        bottomNavigationView.selectedItemId = R.id.navigation_informe_usuario
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home_usuario -> {
                    startActivity(Intent(this, Ejercicios::class.java))
                    true
                }
                R.id.navigation_ejercicios_usuario -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_informe_usuario -> {
                    startActivity(Intent(this, InformesActivity::class.java))
                    true
                }
                R.id.navigation_perfil_usuario -> {
                    startActivity(Intent(this, PerfilUsuarioActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun calcularIMC() {
        val pesoStr = pesoEditText.text.toString()
        val alturaStr = alturaEditText.text.toString()

        if (pesoStr.isNotEmpty() && alturaStr.isNotEmpty()) {
            val peso = pesoStr.toDouble()
            val altura = alturaStr.toDouble() / 100.0 // Convertir altura de cm a m

            val imc = peso / (altura * altura)

            // Actualiza la barra de progreso
            actualizarBarraDeProgreso(imc)

            // Muestra el resultado en el TextView
            mostrarResultado(imc)
        }
    }

    private fun actualizarBarraDeProgreso(imc: Double) {
        // Ajusta el progreso de la barra de progreso según el valor del IMC
        progressBarIMC.progress = imc.toInt()
    }

    private fun mostrarResultado(imc: Double) {
        val mensaje: String = when {
            imc < 18.5 -> getString(R.string.bajo_peso)
            imc < 25 -> getString(R.string.peso_normal)
            imc < 30 -> getString(R.string.sobrepeso)
            else -> getString(R.string.obesidad)
        }

        val resultado = getString(R.string.resultado_imc, imc, mensaje)
        resultadoTextView.text = resultado
    }
}