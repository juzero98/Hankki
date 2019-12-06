package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.annotation.ContentView
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
        var numString = num.toString()
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

        menuname.setText(menu)

        val submit_btn = findViewById<Button>(R.id.submit)
        submit_btn.setOnClickListener {
            db.collection("reviews")
                .orderBy("count")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val count = document.get("count").toString()
                        //val numInt = Integer.parseInt(count)
                        if(num == Integer.parseInt(count)){
                            num++
                        }
                    }
                    numString = num.toString()
                    val writeReview = ReviewData(starNum.toFloat(), editText.text.toString(),userId ,menu, num)
                    db.collection("reviews").document(numString).set(writeReview)
                }
            finish()
        }
    }

}
