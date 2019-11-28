package com.example.hankki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_community.*

class CommunityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val id = intent.getStringExtra("id")


        writeBtn.setOnClickListener{
            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        showBtn.setOnClickListener{
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
        }
    }
}
