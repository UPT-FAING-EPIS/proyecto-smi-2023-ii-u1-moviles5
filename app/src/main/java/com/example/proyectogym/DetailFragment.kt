package com.example.proyectogym

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class DetailFragment : Fragment() {
    
    private var idVideoGam: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

            idVideoGam = arguments?.getInt("id", 0)!!
            
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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