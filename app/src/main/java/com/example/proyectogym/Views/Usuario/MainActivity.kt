package com.example.proyectogym.Views.Usuario

import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.example.proyectogym.Controllers.CategoriaController
import com.example.proyectogym.Models.Categoria
import com.example.proyectogym.R
import com.example.proyectogym.Views.Admin.HomeActivity
import com.example.proyectogym.Views.Admin.ListEjerciciosActivity
import com.example.proyectogym.Views.Admin.PerfilAdminActivity
import com.example.proyectogym.Views.Admin.ZonaActivity
import com.example.proyectogym.Views.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {

    private lateinit var addButton: Button
    private val categoriaController = CategoriaController()
    private lateinit var listaDeCategorias: MutableList<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Obtener categorías antes de inflar la vista
        obtenerCategorias { categorias ->
            listaDeCategorias = categorias.toMutableList()
            setContentView(R.layout.activity_main)

            val user = UserSingleton.getInstance()
            setup(user.correo ?: "")

            listaDeCategorias = listaDeCategorias.distinctBy { it.imagen }.toMutableList()

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            for (categoria in listaDeCategorias) {
                val nombre = categoria.nombre
                val nivel = categoria.nivel
                val imagenUrl = categoria.imagen

                val nuevoElemento = RelativeLayout(this)

                val imageView = ImageView(this)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )

                val marginInDp = 10
                val marginInPx = dpToPx(marginInDp)
                val params = imageView.layoutParams as RelativeLayout.LayoutParams
                params.setMargins(0, 0, 0, marginInPx)
                imageView.layoutParams = params

                val textoView = TextView(this)
                textoView.text = nombre
                textoView.setTypeface(null, Typeface.BOLD)
                textoView.textSize = 18.0f

                val paddingInDp = 10
                val paddingInPx = dpToPx(paddingInDp)
                textoView.setPadding(paddingInPx, paddingInPx, paddingInPx, 0)

                val contenedorId: Int = when (nivel) {
                    "Principiante" -> R.id.principianteContainer
                    "Intermedio" -> R.id.intermedioContainer
                    "Avanzado" -> R.id.avanzadoContainer
                    else -> 0
                }

                if (contenedorId != 0) {
                    val contenedor = findViewById<LinearLayout>(contenedorId)
                    nuevoElemento.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    contenedor.addView(nuevoElemento)
                    nuevoElemento.addView(imageView)
                    nuevoElemento.addView(textoView)

                    // Cargar la imagen en el ImageView usando Picasso
                    val imageReference = storageRef.child("categoria/$imagenUrl")
                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        Picasso.get().load(uri).into(imageView)
                    }.addOnFailureListener { e ->
                        // Manejar errores
                    }
                }

                imageView.setOnClickListener {
                    val intent = Intent(this, ListEjerciciosUsuarioActivity::class.java)

                    intent.putExtra("imagenUrl", imagenUrl)
                    intent.putExtra("textoCategoria", nombre)

                    startActivity(intent)
                }
            }

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation_usuario)
            bottomNavigationView.selectedItemId = R.id.navigation_ejercicios_usuario
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
    }

    //Obtiene los datos del login
    private fun setup(email: String) {
        title = "Inicio"

        val emailTextView = findViewById<TextView>(R.id.textNombreApellido)
        emailTextView.text = email

    }

    private fun obtenerCategorias(callback: (List<Categoria>) -> Unit) {
        categoriaController.obtenerCategorias { categorias ->
            if (categorias != null) {
                // Llenar listaDeCategorias
                if (!::listaDeCategorias.isInitialized) {
                    listaDeCategorias = mutableListOf()
                }

                for (categoria in categorias) {
                    val nombre = categoria.nombre
                    val nivel = categoria.nivel
                    val imagen = categoria.imagen
                    val categoriaCompleta = Categoria(nombre, nivel, imagen)
                    listaDeCategorias.add(categoriaCompleta)
                }

                callback(listaDeCategorias)
            } else {
                callback(emptyList())
            }
        }
    }

    fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

}