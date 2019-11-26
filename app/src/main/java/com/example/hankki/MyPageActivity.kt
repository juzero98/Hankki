package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*

class MyPageActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
//    private val userData = ArrayList<User>()
    private val reviewData = ArrayList<MyPage>()
    private var readSucess  = false
    private val datas = arrayOfNulls<String>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        readUser()
        readReview()

    }

    // 로그인 한 사용자 이름, 잔액 표시
    private fun readUser() {
        val userId = intent.getStringExtra("id")
        db.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    datas[0] = document.get("id").toString()
                    datas[1] = document.get("name").toString()
                    datas[2] = document.get("cash").toString()
                }
                user_name.text = datas[1]//.toString()
                user_cash.text = datas[2]//.toString()
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }

    private fun readReview() {
        val userId = intent.getStringExtra("id")
        db.collection("reviews")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val star = document.get("star").toString()
                    val review = document.get("review").toString()
                    val menu = document.get("menu").toString()
                    if (!readSucess) {
                        reviewData.add(MyPage(menu, star.toFloat()  ,review, img))
                    }
                }
                upload()
                readSucess = true

            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }
    private fun upload() {
        val mGrid = grid
        val mAdapter = MyAdapter(this, reviewData)
        mGrid.adapter = mAdapter

    }
}
