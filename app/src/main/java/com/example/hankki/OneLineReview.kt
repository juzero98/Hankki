package com.example.hankki

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*

data class OnelineData(var menuname: String, var id: String, var review: String)

class OneLineReview : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val onelineData = ArrayList<OnelineData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.oneline_review)
        readReview()

    }

    fun readReview() {
        val menu = intent.getStringExtra("menu")
        db.collection("reviews")
            .whereEqualTo("menu", menu)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                   // val menuname = document.get("menu").toString()
                    val id = document.get("id").toString()
                    val review = document.get("review").toString()

                    onelineData.add(OnelineData(menu,id, review))

                }
                upload()


            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }

        val submit_btn = findViewById<Button>(R.id.submit)
        submit_btn.setOnClickListener {
            finish()
        }
    }


    fun upload() {
        val mGrid = grid
        val mAdapter = OneLineAdapter(this, onelineData)
        mGrid.adapter = mAdapter

    }
}