package com.example.hankki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_write.*
import java.util.*

class WriteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        val db = FirebaseFirestore.getInstance()
        val id = intent.getStringExtra("id")

        val random = Random()
        val randomNum = random.nextInt(100).toString()

        val size = 0

        fun getCount(ref: DocumentReference): Task<Int> {
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
        }

        //db.collection("board").get().then(function(querySnapshot) {
        //    console.log(querySnapshot.size)
        //})


        writeButton.setOnClickListener{
            val writeTitle: String = writeTitle.text.toString()
            val writeContent: String = writeContent.text.toString()

            val board = Board(writeContent, id, writeTitle)
            db.collection("board").document(randomNum).set(board)

            val intent = Intent(this, CommunityActivity::class.java)

            startActivity(intent)



        }
    }
}