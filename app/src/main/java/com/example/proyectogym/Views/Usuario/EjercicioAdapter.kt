package com.example.proyectogym.Views.Usuario

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectogym.Models.Ejercicio
import com.example.proyectogym.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso



class EjercicioAdapter(private val dataList: List<Ejercicio>) :
    RecyclerView.Adapter<EjercicioAdapter.MyViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreTextView: TextView = itemView.findViewById(R.id.tituloTextView)
        val gifImageView: ImageView = itemView.findViewById(R.id.gifImageView)
        val subtituloTextView: TextView = itemView.findViewById(R.id.subtituloTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ejercicio, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ejercicio = dataList[position]

        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imageReference = storageRef.child("ejercicios/${dataList[position].imagen}")

        val context = holder.itemView.context

        imageReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context)
                .asGif()  // Indicar que estamos cargando un GIF
                .load(uri)
                .into(holder.gifImageView)
        }.addOnFailureListener { e ->
            Log.e("FirebaseStorage", "Error al cargar la imagen desde Firebase Storage: $e")
        }

        holder.nombreTextView.text = dataList[position].titulo
        if(dataList[position].cantidad > 0){
            holder.subtituloTextView.text = "x${dataList[position].cantidad}"
        }else{
            val segundosTotales = dataList[position].tiempo
            val minutos = segundosTotales / 60
            val segundos = segundosTotales % 60

            val tiempoFormateado = String.format("%02d:%02d", minutos, segundos)
            holder.subtituloTextView.text = tiempoFormateado
        }

        // Manejar clic del elemento
        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(ejercicio.id)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}