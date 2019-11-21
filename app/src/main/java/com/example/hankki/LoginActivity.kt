package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = FirebaseFirestore.getInstance()
        val loginBtn = findViewById<Button>(R.id.loginBtn)

        loginBtn.setOnClickListener {
            val userId1 = userId.text.toString()
            val userPw1 = userPw.text.toString()
            db.collection("users")
                .whereEqualTo("id", userId1)
                .whereEqualTo("pw", userPw1)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                        val id = document.get("id").toString()
                        val intent = Intent(this, SelectActivity::class.java)

                        intent.putExtra("id", id)

                        startActivity(intent)
                        //startActivity(intent1)
                        //  val intent = Intent(this, SelectActivity::class.java)
                        //  startActivity(intent)
                    }
                }

        }
    }
}