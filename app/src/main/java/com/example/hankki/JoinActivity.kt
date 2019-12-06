package com.example.hankki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*


class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)
        val db = FirebaseFirestore.getInstance()
        joinBtn.setOnClickListener{
            val name: String = userName.text.toString()
            val id: String = userId.text.toString()
            val pw: String = userPw.text.toString()
            val user = User(name, id, pw, 0)
            db.collection("users").document(name).set(user)

            finish()
        }




    }
}