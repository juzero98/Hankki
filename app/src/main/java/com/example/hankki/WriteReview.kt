package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.give_review.*


class WriteReview : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    //    private val writeReview = ArrayList<ReviewData>()
    var starNum = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.give_review)
        var num = 1
        val prefs = getSharedPreferences("user", 0)
        var userId = prefs.getString("id", "")

        val plusStar_Click = findViewById<Button>(R.id.plusStar)
        plusStar_Click.setOnClickListener {
            starNum += 0.5
            star.rating = starNum.toFloat()
        }
        val minusStar_Click = findViewById<Button>(R.id.minusStar)
        minusStar_Click.setOnClickListener {
            starNum -= 0.5
            star.rating = starNum.toFloat()
        }
        val editText = findViewById<EditText>(R.id.writeReview)

        val menu = intent.getStringExtra("menu")

        val submit_btn = findViewById<Button>(R.id.submit)
        submit_btn.setOnClickListener {
            val writeReview = ReviewData(starNum.toFloat(), editText.text.toString(),userId ,menu)
            db.collection("reviews")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.equals(num))
                            num++
                        else
                            db.collection("reviews").document().set(writeReview)

                    }
                }
            finish()
        }
    }
}