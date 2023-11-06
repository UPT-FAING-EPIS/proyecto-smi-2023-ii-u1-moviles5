package com.example.proyectogym

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView

class DetailActivity :YouTubeBaseActivity() , View.OnClickListener {

    private lateinit var btn1: Button
    private lateinit var btn2: Button
    private lateinit var btn3: Button

    var KEY_API = "AIzaSyB8GiDYzjKuL3inX4W5xIXNDKG66MT--cI"
    //Instancia de la vista(reproductor)
    private lateinit var youtubeplayer: YouTubePlayerView
    //Listener del video
    private lateinit var youtubeplayerinit: YouTubePlayer.OnInitializedListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        val idVideoGame = intent.getIntExtra("id", 0)
        val nameTextView: TextView = findViewById(R.id.nameTextView)
        val dateTextView: TextView = findViewById(R.id.dateTextView)
        val ratingTextView: TextView = findViewById(R.id.ratingTextView)
        val descriptionTextView: TextView = findViewById(R.id.descriptionTextView)
        val posterImageView: ImageView = findViewById(R.id.posterImageView)

        nameTextView.text = resources.getStringArray(R.array.name)[idVideoGame]
        dateTextView.text = resources.getStringArray(R.array.date)[idVideoGame]
        ratingTextView.text = resources.getStringArray(R.array.rating)[idVideoGame]
        descriptionTextView.text = resources.getStringArray(R.array.description)[idVideoGame]

        Glide.with(this)
            .load(resources.getStringArray(R.array.url)[idVideoGame])
            .into(posterImageView)

        btn1 = findViewById(R.id.btn1)
        btn2 = findViewById(R.id.btn2)
        btn3   = findViewById(R.id.btn3 )

        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)

        youtubeplayer = findViewById(R.id.video_player)

    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btn1 -> {

                    start("EtJGUHn7uSE")
                }
                R.id.btn2 -> {
                    start("SasACgGscHg")
                }
                R.id.btn3 -> {
                    start("frNjBYk3SRw")
                }
            }
        }
    }
    private fun start( ID: String){
        youtubeplayerinit = object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(
                p0: YouTubePlayer.Provider?,
                p1: YouTubePlayer?,
                p2: Boolean
            ) {

                p1?.loadVideo(ID)
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Toast.makeText(applicationContext, "fallo:"+p1, Toast.LENGTH_SHORT).show()
            }
        }

        youtubeplayer.initialize(KEY_API,youtubeplayerinit)
    }
}