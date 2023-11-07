    package com.example.proyectogym.Views.Admin
    import android.app.AlertDialog
    import android.content.Intent
    import android.graphics.Bitmap
    import android.graphics.Typeface
    import android.graphics.drawable.BitmapDrawable
    import android.os.Bundle
    import android.view.LayoutInflater
    import android.view.View
    import android.widget.Button
    import android.widget.EditText
    import android.widget.ImageView
    import android.widget.LinearLayout
    import android.widget.RelativeLayout
    import android.widget.Spinner
    import android.widget.TextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import com.example.proyectogym.Controllers.CategoriaController
    import com.example.proyectogym.Controllers.ZonaController
    import com.example.proyectogym.Models.Categoria
    import com.example.proyectogym.Models.Zona
    import com.example.proyectogym.R
    import com.example.proyectogym.Views.UserSingleton
    import com.google.android.material.bottomnavigation.BottomNavigationView
    import com.google.firebase.storage.FirebaseStorage
    import com.squareup.picasso.Picasso
    import java.io.ByteArrayOutputStream
    import java.util.UUID

    class ZonaActivity : AppCompatActivity() {

        private lateinit var addButton: Button
        private val zonaController = ZonaController()
        private lateinit var listaDeZonas: MutableList<Zona>


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            obtenerZonas { zonas ->
                listaDeZonas = zonas.toMutableList()
                setContentView(R.layout.activity_zona)

                addButton = findViewById(R.id.buttonAddZonas)
                val user = UserSingleton.getInstance()
                setup(user.correo ?: "")

                addButton.setOnClickListener {
                    showPopup()
                }

                listaDeZonas = listaDeZonas.distinctBy { it.nombre }.toMutableList()

                var indiceLinear = 1
                for (zona in listaDeZonas) {
                    val nombre = zona.nombre
                    val estado = zona.estado

                    val nuevoElemento = LinearLayout(this)
                    val layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    nuevoElemento.layoutParams = layoutParams
                    nuevoElemento.orientation = LinearLayout.VERTICAL
                    val padding16dp = resources.getDimensionPixelSize(R.dimen.padding_16dp)
                    val padding8dp = resources.getDimensionPixelSize(R.dimen.padding_8dp)

                    nuevoElemento.setPadding(padding16dp, padding8dp, 0, 0)


                    val textoView = TextView(this)
                    textoView.text = nombre
                    textoView.setTypeface(null, Typeface.BOLD)
                    textoView.textSize = 18.0f

                    val nuevoElementoHijo = LinearLayout(this)
                    val layoutParamsHijo = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    nuevoElementoHijo.layoutParams = layoutParamsHijo
                    nuevoElementoHijo.orientation = LinearLayout.HORIZONTAL

                    val textoViewEstado = TextView(this)
                    textoViewEstado.text = "Estado: "
                    textoViewEstado.textSize = 14.0f

                    val textoViewEstadoValue = TextView(this)
                    textoViewEstadoValue.text = estado
                    textoViewEstadoValue.textSize = 14.0f

                    nuevoElementoHijo.addView(textoViewEstado)
                    nuevoElementoHijo.addView(textoViewEstadoValue)
                    nuevoElemento.addView(textoView)
                    nuevoElemento.addView(nuevoElementoHijo)

                    val parentLayout = addButton.parent as LinearLayout
                    val index = parentLayout.indexOfChild(addButton)
                    parentLayout.addView(nuevoElemento, index + indiceLinear)

                    val separador = View(this)
                    val separadorLayoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        resources.getDimensionPixelSize(R.dimen.layout_1dp)
                    )
                    separadorLayoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.layout_Top4dp)
                    separador.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
                    separador.layoutParams = separadorLayoutParams

                    indiceLinear++
                }

                val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
                bottomNavigationView.selectedItemId = R.id.navigation_zonas
                bottomNavigationView.setOnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.navigation_home -> {
                            startActivity(Intent(this, HomeActivity::class.java))
                            true
                        }
                        R.id.navigation_zonas -> {
                            startActivity(Intent(this, ZonaActivity::class.java))
                            true
                        }
                        R.id.navigation_perfil -> {
                            startActivity(Intent(this, PerfilAdminActivity::class.java))
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

            val emailTextView = findViewById<TextView>(R.id.textNombreApellidoZonas)
            emailTextView.text = email

        }

        private fun obtenerZonas(callback: (List<Zona>) -> Unit) {
            zonaController.obtenerZonas { zonas ->
                if (zonas != null) {
                    // Llenar listaDeCategorias
                    if (!::listaDeZonas.isInitialized) {
                        listaDeZonas = mutableListOf()
                    }

                    for (zona in zonas) {
                        val nombre = zona.nombre
                        val estado = zona.estado
                        val zonaCompleta = Zona(nombre, estado)
                        listaDeZonas.add(zonaCompleta)
                    }
                    callback(listaDeZonas)

                } else {
                    callback(emptyList())
                }
            }
        }

        private fun showPopup() {

            val builder = AlertDialog.Builder(this)
            val inflater = LayoutInflater.from(this)
            val dialogView = inflater.inflate(R.layout.popup_zona, null)
            builder.setView(dialogView)

            val nombreZonaText = dialogView.findViewById<EditText>(R.id.nombreZonaEditText)
            val cboEstados = dialogView.findViewById<Spinner>(R.id.cboZonaEstados)


            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }

            builder.setPositiveButton("Guardar") { dialog, _ ->
                val nombre = nombreZonaText.text.toString()
                val nivel = cboEstados.selectedItem.toString()

                val nuevaZona = Zona(
                    nombre, nivel
                )
                zonaController.agregarZona(nuevaZona) { exito ->
                    if (exito) {
                        Toast.makeText(
                            this,
                            "Zona registrada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al registrar la zona",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }

            val alertDialog = builder.create()
            alertDialog.show()
        }
    }