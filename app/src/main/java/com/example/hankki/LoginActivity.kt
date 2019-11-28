package com.example.hankki

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



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

                        // preference 사용하여 user.xml에 user 정보 저장
                        val prefs : SharedPreferences = getSharedPreferences("user", 0)
                        val editor : SharedPreferences.Editor = prefs.edit()
                        editor.putString("id", id)
                        editor.apply()

                        val intent = Intent(this, SelectActivity::class.java)
                        startActivity(intent)

                    }
                }

        }
    }
}