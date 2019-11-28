package com.example.hankki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val db = FirebaseFirestore.getInstance()
        val id = intent.getStringExtra("id")
        var num = 1

        /*val random = Random()
        val randomNum = random.nextInt(100).toString()*/

        writeButton.setOnClickListener{

            /*db.collection("board").document(randomNum).set(board)*/
            var numString = num.toString()


            db.collection("board")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val count = document.get("count").toString()
                        val numInt = Integer.parseInt(count)

                        if(num == numInt){

                            val writeTitle: String = writeTitle.text.toString()
                            val writeContent: String = writeContent.text.toString()

                            //document 이름이 numString 인 애가 있다면
                            num++
                            numString = num.toString()
                            val board = Board(writeContent, id, writeTitle, num)
                            db.collection("board").document(numString).set(board)
                        }

/*
                        else{
                            val board = Board(writeContent, id, writeTitle, num)
                            //document 이름이 numString 인 애가 있다면
                            db.collection("board").document(numString).set(board)
                        }*/
                        /* db.collection("board").document(num.toString()).set(board)*/
                        //board 에 집어넣음
                    }
                }



            /*fun getCount(ref: DocumentReference): Task<Int> { //count 필드의 합 가져오기 => document의 개수
                // Sum the count of each shard in the subcollection
                return ref.collection("board").get()
                    .continueWith { task ->
                        var count = 0
                        for (snap in task.result!!) {
                            val shard = snap.toObject(Shard::class.java)
                            count += shard.count
                        }
                        count
                    }
            }*/

            finish()//신기해
  /*          val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)*/

        }
    }
}