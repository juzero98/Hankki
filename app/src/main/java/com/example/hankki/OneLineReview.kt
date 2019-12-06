package com.example.hankki

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*

data class OnelineData(var menuname: String, var id: String, var review: String)

//SeeReviewActivity의 각 fragment의 메뉴들을 눌렀을 때 한줄평을 띄워주는 액티비티

class OneLineReview : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val onelineData = ArrayList<OnelineData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.oneline_review)
        readReview()
    }

    //firestore에서 menu 컬렉션을 가져와서 menu 이름이 같은 애들을 가져온다
    private fun readReview() {
        val menu = intent.getStringExtra("menu")
        db.collection("reviews")
            .whereEqualTo("menu", menu)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                   // val menuname = document.get("menu").toString()
                    val id = document.get("id").toString()
                    val review = document.get("review").toString()

                    onelineData.add(OnelineData(menu,id, review))

                }
                upload()


            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }

        val submit_btn = findViewById<Button>(R.id.submit)
        submit_btn.setOnClickListener {
            finish()
        }
    }

    // 리뷰들을 리스트에 넣어준다
    fun upload() {
        val mGrid = grid
        val mAdapter = OneLineAdapter(this, onelineData)
        mGrid.adapter = mAdapter

    }
}