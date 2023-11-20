package com.example.proyectogym.Views.Usuario

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.proyectogym.R
import com.example.proyectogym.Views.UserSingleton

class ObjetivoActivity : AppCompatActivity() {

    private var selectedButton: Button? = null
    private val defaultButtonBackgroundResId: Int = R.drawable.fondo_blanco_con_borde
    private var selectedButtonText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objetivo)

        val buttonPerder = findViewById<Button>(R.id.buttonPerder)
        val buttonAumentar = findViewById<Button>(R.id.buttonAumentar)
        val buttonMantener = findViewById<Button>(R.id.buttonMantener)
        val buttonSiguiente = findViewById<Button>(R.id.buttonSiguiente2)

        buttonPerder.setOnClickListener { onButtonClick(it) }
        buttonAumentar.setOnClickListener { onButtonClick(it) }
        buttonMantener.setOnClickListener { onButtonClick(it) }

        buttonSiguiente.setOnClickListener {
            if (selectedButton != null) {
                val usuario = UserSingleton.getInstance()
                usuario.objetivo = selectedButtonText

                val intent = Intent(this@ObjetivoActivity, ObjetivoZonaActivity::class.java)
                startActivity(intent)
            }
        }
    }

    fun onButtonClick(view: View) {
        val clickedButton = view as Button

        if (clickedButton != selectedButton) {
            selectedButton?.setBackgroundResource(defaultButtonBackgroundResId)
            selectedButton?.setTextColor(resources.getColor(R.color.black))

            clickedButton.setBackgroundResource(R.color.app_theme)
            clickedButton.setTextColor(resources.getColor(android.R.color.white))
            selectedButton = clickedButton
            selectedButtonText = clickedButton.text.toString()

        } else {

            clickedButton.setBackgroundResource(defaultButtonBackgroundResId)
            clickedButton.setTextColor(resources.getColor(R.color.black))

            selectedButton = null
            selectedButtonText = ""
        }
    }
}