package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val commuData = ArrayList<MyCommunity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val id = intent.getStringExtra("id")

        readFirestore()

        writeBtn.setOnClickListener{
            val intent = Intent(this, WriteActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        showBtn.setOnClickListener{
            val intent = Intent(this, ShowActivity::class.java)
            startActivity(intent)
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
