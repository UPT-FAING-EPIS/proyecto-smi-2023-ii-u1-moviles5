package com.example.proyectogym

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.bumptech.glide.Glide
import com.example.proyectogym.databinding.ActivityDetail3Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.item_list.*

class DetailActivity3 : AppCompatActivity() {

    private lateinit var bindingActivityDetail: ActivityDetail3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        bindingActivityDetail = ActivityDetail3Binding.inflate(layoutInflater)
        val view = bindingActivityDetail.root
        setContentView(view)

        val key = intent.getStringExtra("key")
        val database = Firebase.database
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val myRef =
            database.getReference("game").child(
                key.toString()
            )

        myRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val mControllerProductos: ControllerProductos? =
                    dataSnapshot.getValue(ControllerProductos::class.java)
                if (mControllerProductos != null) {
                    bindingActivityDetail.nameTextView.text = mControllerProductos.name.toString()
                    bindingActivityDetail.dateTextView.text = mControllerProductos.date.toString()
                    bindingActivityDetail.priceTextView.text =
                        mControllerProductos.price.toString() + " .S/"
                    bindingActivityDetail.descriptionTextView.text =
                        mControllerProductos.description.toString()

                    Glide.with(view)
                        .load(mControllerProductos.url.toString())
                        .into(bindingActivityDetail.posterImgeView)



                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("TAG", "Failed to read value.", error.toException())
            }
        })

        findViewById<Button>(R.id.btnComprar).setOnClickListener {
            startActivity(Intent(this, PasarelaPagoActivity::class.java))
        }
    }


}