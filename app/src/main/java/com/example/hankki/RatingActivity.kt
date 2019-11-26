package com.example.hankki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_rating.*

class RatingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val id = intent.getStringExtra("id")

        seeReview.setOnClickListener {
            val intent = Intent(this, SeeReviewActivity::class.java)
            startActivity(intent)
        }

        giveReview.setOnClickListener {
            val intent1 = Intent(this, GiveReviewActivity::class.java)

            intent1.putExtra("id", id)
            startActivity(intent1)
        }

    }
}