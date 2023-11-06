package com.example.proyectogym

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectogym.databinding.ActivityMain3Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage

class MainActivity3 : AppCompatActivity() {

    private lateinit var bindingActivityMain: ActivityMain3Binding
    private lateinit var messagesListener: ValueEventListener

    private val database = Firebase.database
    private val listControllerProductos:MutableList<ControllerProductos> = ArrayList()
    private val myRef = database.getReference("game")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("SOY MONGOLO!")

        bindingActivityMain = ActivityMain3Binding.inflate(layoutInflater)
        val view = bindingActivityMain.root
        setContentView(view)

        bindingActivityMain.addImageView.setOnClickListener { v ->
            val intent = Intent(this, AddActivity3::class.java)
            v.context.startActivity(intent)
        }

        listControllerProductos.clear()
        setupRecyclerView(bindingActivityMain.recyclerView)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        messagesListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                listControllerProductos.clear()
                dataSnapshot.children.forEach { resp ->
                    val mVideoGame =
                        ControllerProductos(resp.child("name").value as String?,
                            resp.child("date").value as String?,
                            resp.child("price").value as String?,
                            resp.child("description").value as String?,
                            resp.child("url").value as String?,
                            resp.key)
                    mVideoGame.let { listControllerProductos.add(it) }
                }
                recyclerView.adapter = VideogameViewAdapter(listControllerProductos)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "messages:onCancelled: ${error.message}")
            }
        }
        myRef.addValueEventListener(messagesListener)

        deleteSwipe(recyclerView)
    }

    class VideogameViewAdapter(private val values: List<ControllerProductos>) :
        RecyclerView.Adapter<VideogameViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.video_game_content, parent, false)
            return ViewHolder(view)
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val mVideoGame = values[position]
            holder.mNameTextView.text = mVideoGame.name
            holder.mDateTextView.text = mVideoGame.date
            holder.mPriceTextView.text = mVideoGame.price + " S./"
            holder.mPosterImgeView.let {
                Glide.with(holder.itemView.context)
                    .load(mVideoGame.url)
                    .into(it)
            }

            holder.itemView.setOnClickListener { v ->
                val intent = Intent(v.context, DetailActivity3::class.java).apply {
                    putExtra("key", mVideoGame.key)
                }
                v.context.startActivity(intent)
            }

            holder.itemView.setOnLongClickListener{ v ->
                val intent = Intent(v.context, EditActivity3::class.java).apply {
                    putExtra("key", mVideoGame.key)
                }
                v.context.startActivity(intent)
                true
            }

        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val mNameTextView: TextView = view.findViewById(R.id.nameTextView) as TextView
            val mDateTextView: TextView = view.findViewById(R.id.dateTextView) as TextView
            val mPriceTextView: TextView = view.findViewById(R.id.priceTextView) as TextView
            val mPosterImgeView: ImageView = view.findViewById(R.id.posterImgeView) as ImageView
        }
    }

    private fun deleteSwipe(recyclerView: RecyclerView){
        val touchHelperCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val imageFirebaseStorage = FirebaseStorage.getInstance().reference.child("game/img"+listControllerProductos[viewHolder.adapterPosition].key)
                imageFirebaseStorage.delete()

                listControllerProductos[viewHolder.adapterPosition].key?.let { myRef.child(it).setValue(null) }
                listControllerProductos.removeAt(viewHolder.adapterPosition)

                recyclerView.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
        val itemTouchHelper = ItemTouchHelper(touchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}