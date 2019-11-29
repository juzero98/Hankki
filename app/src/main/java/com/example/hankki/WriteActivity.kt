package com.example.hankki

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_write.*

class WriteActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val commuData = ArrayList<MyCommunity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

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
                    for (document in documents ) {
                        val count = document.get("count").toString()
                        val numInt = Integer.parseInt(count)
                        if(num == numInt){
                            num++
                        }
                    }

                    val writeTitle: String = writeTitle.text.toString()
                    val writeContent: String = writeContent.text.toString()

                    //document 이름이 numString 인 애가 있다면
                    numString = num.toString()
                    val board = Board(writeContent, id, writeTitle, num)
                    db.collection("board").document(numString).set(board)

                    readFirestore()
                }

            finish()//신기해
  /*          val intent = Intent(this, CommunityActivity::class.java)
            startActivity(intent)*/

        }
    }

    fun readFirestore(){ //db 읽어와
        db.collection("board")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents ) {
                    var title = document.get("title").toString()
                    var content = document.get("content").toString()

                    commuData.add(MyCommunity(title, content))
                }
                upload()
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }

    fun upload(){ //gridview에 upload
        val mGrid = grid
        val mAdapter = CommunityAdapter(this, commuData)
        mGrid.adapter = mAdapter
    }
}