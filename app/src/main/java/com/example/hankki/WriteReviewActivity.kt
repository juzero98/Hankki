package com.example.hankki

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.give_review.*

// 먹은 음식들만 리뷰를 쓸 수 있는 액티비티
class WriteReview : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    var starNum = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.give_review)
        var num = 1
        var numString = num.toString()
        val prefs = getSharedPreferences("user", 0)
        var userId = prefs.getString("id", "")

        val plusStar_Click = findViewById<Button>(R.id.plusStar)
        plusStar_Click.setOnClickListener { //플러스를 누르면 0.5씩 증가
            starNum += 0.5
            star.rating = starNum.toFloat()
        }
        val minusStar_Click = findViewById<Button>(R.id.minusStar)
        minusStar_Click.setOnClickListener {//감소를 누르면 0.5씩 감소
            starNum -= 0.5
            star.rating = starNum.toFloat()
        }
        val editText = findViewById<EditText>(R.id.writeReview)

        val menu = intent.getStringExtra("menu")

        menuname.setText(menu)

        val submit_btn = findViewById<Button>(R.id.submit)
        //reviews 컬렉션을 count 필드로 정렬
        submit_btn.setOnClickListener {
            db.collection("reviews")
                .orderBy("count")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val count = document.get("count").toString()
                        //그 필드가 있으면 num ++ 하고 넘어가면서
                        if(num == Integer.parseInt(count)){
                            num++
                        }
                    }
                    //numString에 넣어주면 numString을 document 이름으로 바꿔주고 그 필드는 writeReview 데이터 클래스로 넣어준다
                    numString = num.toString()
                    val writeReview = ReviewData(starNum.toFloat(), editText.text.toString(),userId ,menu, num)
                    db.collection("reviews").document(numString).set(writeReview)
                }
            finish()
        }
    }

}
