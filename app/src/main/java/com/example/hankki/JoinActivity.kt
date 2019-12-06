package com.example.hankki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*

// User data를 나타내는 class
data class User (var name: String, var id: String, var pw: String, var cash: Int)

// 회원가입 Activity
class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val db = FirebaseFirestore.getInstance()
        // Join 버튼 눌렀을 시
        joinBtn.setOnClickListener{
            val name: String = userName.text.toString()
            val id: String = userId.text.toString()
            val pw: String = userPw.text.toString()
            val user = User(name, id, pw, 0)
            db.collection("users").document(name).set(user)

            this.finish()
        }
    }
}