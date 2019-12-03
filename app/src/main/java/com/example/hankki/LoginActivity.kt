package com.example.hankki

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
                //.whereEqualTo("pw", userPw1)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                        if(!document.get("pw").toString().equals(userPw1))
                            showDialog()

                        else{
                            val id = document.get("id").toString()
                            val cash = document.get("cash").toString()
                            val name = document.get("name").toString()
                            val intent = Intent(this, SelectActivity::class.java)
                            val cashTypeInt = Integer.parseInt(cash)

                            // preference 사용하여 user.xml에 user 정보 저장
                            val prefs : SharedPreferences = getSharedPreferences("user", 0)
                            val editor : SharedPreferences.Editor = prefs.edit()
                            editor.putString("id", id)
                            editor.putInt("cash",cashTypeInt)
                            editor.putString("name",name)
                            editor.apply()

                            intent.putExtra("id", id)
                            //  intent.putExtra("cash",cash)
                            //  intent.putExtra("name",name)
                            //이름 여기까지 넣어주고 끝났음

                            startActivity(intent)
                        }

                    }
                }
        }
    }

    fun showDialog(){
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        builder.setTitle("경고")
        val dialogLayout = inflater.inflate(R.layout.loginshow_dialog,null)

        builder.setPositiveButton("OK"){dialogInterface, i ->

        }

        builder.setView(dialogLayout)
        builder.show()
    }
}