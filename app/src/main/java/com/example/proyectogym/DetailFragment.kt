package com.example.proyectogym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {

    //declaramos un valor idVideoGame en 0
    private var idVideoGam: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            //utilizamos para asignar una variable a IdvideoGame
            idVideoGam = arguments?.getInt("id", 0)!!
            
        }
    }
    //implementación de la función onCreateView de un fragmento en Android.
    // Esta función se llama cuando el sistema necesita que el fragmento
    // infle su diseño y lo presente en la pantalla
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //La función devuelve un objeto View que representa el diseño del fragmento.
        // El código dentro de la función busca las vistas dentro del diseño del fragmento
        // utilizando findViewById, y establece sus valores con la información correspondiente que se encuentra en los arrays
        val view = inflater.inflate(R.layout.fragment_detail, container, false)

        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)
        val ratingTextView: TextView = view.findViewById(R.id.ratingTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

        nameTextView.text = resources.getStringArray(R.array.name)[idVideoGam]
        dateTextView.text = resources.getStringArray(R.array.date)[idVideoGam]
        ratingTextView.text = resources.getStringArray(R.array.rating)[idVideoGam]
        descriptionTextView.text = resources.getStringArray(R.array.description)[idVideoGam]

        return  view
    }

}