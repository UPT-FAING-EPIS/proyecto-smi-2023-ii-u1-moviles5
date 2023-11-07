package com.example.proyectogym.Views.Admin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
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
import com.example.proyectogym.Models.Categoria
import com.example.proyectogym.R
import com.example.proyectogym.Views.UserSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream
import java.util.UUID

class HomeActivity : AppCompatActivity() {

    private lateinit var addButton: Button
    private lateinit var imagenImageView: ImageView
    private val categoriaController = CategoriaController()
    private lateinit var listaDeCategorias: MutableList<Categoria>
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener categorías antes de inflar la vista
        obtenerCategorias { categorias ->
            listaDeCategorias = categorias.toMutableList()
            setContentView(R.layout.activity_home)
            addButton = findViewById(R.id.buttonAdd)

            val user = UserSingleton.getInstance()
            setup(user.correo ?: "")

            addButton.setOnClickListener {
                showPopup()
            }

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
                    val intent = Intent(this, ListEjerciciosActivity::class.java)

                    intent.putExtra("imagenUrl", imagenUrl)
                    intent.putExtra("textoCategoria", nombre)

                    startActivity(intent)
                }
            }

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
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

    private fun showPopup() {

        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.popup_layout, null)
        builder.setView(dialogView)

        val nombreEditText = dialogView.findViewById<EditText>(R.id.nombreCategoriaEditText)
        val cboNivel = dialogView.findViewById<Spinner>(R.id.cboNivelCategoria)
        imagenImageView = dialogView.findViewById(R.id.imagenImageView)
        val seleccionarImagenButton = dialogView.findViewById<Button>(R.id.seleccionarImagenButton)

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.setPositiveButton("Guardar") { dialog, _ ->
            val nombre = nombreEditText.text.toString()
            val nivel = cboNivel.selectedItem.toString()

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference

            val imagenBitmap = (imagenImageView.drawable as BitmapDrawable).bitmap

            val nombreImagen = UUID.randomUUID().toString() + ".jpg"

            // Ruta en Firebase Storage donde deseas guardar la imagen
            val pathEnStorage = "categoria/$nombreImagen"

            // Referencia al archivo en Firebase Storage
            val imagenRef = storageRef.child(pathEnStorage)

            // Sube la imagen a Firebase Storage
            val baos = ByteArrayOutputStream()
            imagenBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val uploadTask = imagenRef.putBytes(data)

            uploadTask.addOnSuccessListener { taskSnapshot ->
                val nuevaCategoria = Categoria(
                    nombre, nivel, nombreImagen
                )
                categoriaController.agregarCategoria(nuevaCategoria) { exito ->
                    if (exito) {
                        Toast.makeText(
                            this,
                            "Categoría registrada con éxito",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this,
                            "Error al registrar la Categoría",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al registrar la Categoría",
                    Toast.LENGTH_SHORT
                ).show()
            }

            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()

        seleccionarImagenButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            val imageUri = data?.data
            imagenImageView.setImageURI(imageUri)
        }
    }
}