package com.example.hankki

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

// 로그인 Activity
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = FirebaseFirestore.getInstance()

        // Login 버튼 클릭 시
        loginBtn.setOnClickListener {
            val userId1 = userId.text.toString()
            val userPw1 = userPw.text.toString()

            // DB 'users'에서 id로 정보 불러와서 비밀번호 맞는지 확인
            db.collection("users")
                .whereEqualTo("id", userId1)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {

                        // 비밀번호 틀렸을 시
                        if(!document.get("pw").toString().equals(userPw1))
                            showDialog()

                        // 비밀번호 맞았을 시
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
                            startActivity(intent)
                        }

                    }
                }
        }
    }

    // 비밀번호 틀렸을 시 뜨는 Dialog
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