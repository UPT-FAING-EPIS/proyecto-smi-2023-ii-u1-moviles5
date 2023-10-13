package com.example.proyectogym.Views

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.proyectogym.R
import com.example.proyectogym.Views.Admin.Ejercicios.ListaEjercicioActivity
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    private lateinit var addButton: Button
    private val REQUEST_PERMISSION = 123
    companion object {
        private const val REQUEST_IMAGE_PICK = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        addButton = findViewById(R.id.buttonAdd)
        val categoryImage = findViewById<ImageView>(R.id.categoryImage)


        addButton.setOnClickListener {
            showPopup()
        }

        categoryImage.setOnClickListener {
            val intent = Intent(this, ListaEjercicioActivity::class.java)
            startActivity(intent)
        }

        val bundle = intent.extras
        val email = bundle?.getString("email")
        setup(email ?: "")
    }

    //Obtiene los datos del login
    private fun setup(email: String) {
        title = "Inicio"

        val emailTextView = findViewById<TextView>(R.id.textNombreApellido)
        emailTextView.text = email

    }

    private fun showPopup() {

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.popup_layout, null)
        builder.setView(dialogView)

        val nombreEditText = dialogView.findViewById<EditText>(R.id.nombreEditText)
        val seleccionarImagenButton = dialogView.findViewById<Button>(R.id.seleccionarImagenButton)
        val imagenImageView = dialogView.findViewById<ImageView>(R.id.imagenImageView)
        val guardarButton = dialogView.findViewById<Button>(R.id.guardarButton)


        seleccionarImagenButton.setOnClickListener {
            // Aquí solicitas permisos de lectura de almacenamiento externo en tiempo de ejecución
            val permission = Manifest.permission.READ_EXTERNAL_STORAGE
            val grant = PackageManager.PERMISSION_GRANTED
            if (ContextCompat.checkSelfPermission(this, permission) != grant) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_PERMISSION)
            } else {
                // Si los permisos ya se han otorgado, puedes abrir la galería aquí
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_PICK)
                } else {
                    Toast.makeText(this, "No se encontró una aplicación para seleccionar imágenes", Toast.LENGTH_SHORT).show()
                }
            }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

    }
}