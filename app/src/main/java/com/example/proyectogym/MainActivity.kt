package com.example.proyectogym

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.proyectogym.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize Firebase Auth
        auth = Firebase.auth

        fullScreen()

        val detailFrameLayout: FrameLayout? = findViewById(R.id.detailFrameLayout)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val onClickListener = View.OnClickListener { itemView ->
            val id = itemView.tag
            if (detailFrameLayout != null) {
                val fragment = DetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt("id", id.toString().toInt())
                    }
                }
                supportFragmentManager.beginTransaction().replace(R.id.detailFrameLayout, fragment).commit()
            } else {
                val intent = Intent(this, DetailActivity::class.java).apply {
                    putExtra("id", id.toString().toInt())
                }
                this.startActivity(intent)
            }
        }

        setupRecyclerView(recyclerView, onClickListener)
    }



    private fun setupRecyclerView(recyclerView: RecyclerView, onClickListener: View.OnClickListener) {
        recyclerView.adapter = VideoGameViewAdapter(getVideoGames(), onClickListener)
    }

    class VideoGameViewAdapter(private val values: List<ControllerRecyclearView>, private val onClickListener: View.OnClickListener) :
        RecyclerView.Adapter<VideoGameViewAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val videoGame = values[position]

            holder.posterImageView.let {
                Glide.with(holder.itemView.context)
                    .load(videoGame.url)
                    .into(it)
            }

            with(holder.itemView) {
                tag = videoGame.id
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val posterImageView: ImageView = view.findViewById(R.id.posterImageView)
        }
    }

    private fun getVideoGames(): MutableList<ControllerRecyclearView>{
        val controllerRecyclearView:MutableList<ControllerRecyclearView> = ArrayList()

        val id = resources.getIntArray(R.array.id)
        val name = resources.getStringArray(R.array.name)
        val date = resources.getStringArray(R.array.date)
        val rating = resources.getStringArray(R.array.rating)
        val description = resources.getStringArray(R.array.description)
        val url = resources.getStringArray(R.array.url)

        for (i in id.indices) {
            controllerRecyclearView.add(ControllerRecyclearView(id[i], name[i], date[i], rating[i], description[i], url[i]))
        }

        return controllerRecyclearView
    }

    private fun fullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val controller = window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}