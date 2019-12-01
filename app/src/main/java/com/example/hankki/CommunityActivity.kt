package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*

class CommunityActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    val commuData = ArrayList<MyCommunity>()

    var compare:Int =0

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

        swipe.setOnRefreshListener {
            afterWrite()

            swipe.isRefreshing = false
        }


    }

   override fun onResume() {
        super.onResume()

       var writeActivity = WriteActivity()
       if(writeActivity.isFinishing){
           afterWrite()
       }
    }

    fun afterWrite(){
        commuData.clear()
        readFirestore()
        /*val mGrid = grid
        val mAdapter = CommunityAdapter(this, commuData)
        mAdapter.notifyDataSetChanged()*/
    }

    fun readFirestore(){ //db 읽어와
        db.collection("board")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents ) {
                    var title = document.get("title").toString()

                    commuData.add(MyCommunity(title))
                }

                commuData.reverse() //최근글이 위로 가게
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

        mGrid.setOnItemClickListener{ parent, view, position, id ->
            showDialog(commuData[position].titleId)
        }

    }

    fun showDialog(titleId:String?){
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("글 보기")
            val dialogLayout = inflater.inflate(R.layout.writeshow_dialog,null)

            val dialogTitle = dialogLayout.findViewById<TextView>(R.id.dialogTitle)
            val dialogContent = dialogLayout.findViewById<TextView>(R.id.dialogContent)
            val dialogUser = dialogLayout.findViewById<TextView>(R.id.dialogUser)

                 db.collection("board")
                .whereEqualTo("title", titleId) //userId1에 그 그리드 뷰의 title
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val stringDialogTitle = document.get("title").toString()
                        val stringDialogContent = document.get("content").toString()
                        val stringDialogUser = document.get("id").toString()

                        dialogTitle.setText(stringDialogTitle)
                        dialogContent.setText(stringDialogContent)
                        dialogUser.setText(stringDialogUser)
                    }
                }

            builder.setPositiveButton("OK"){dialogInterface, i ->

            }

            builder.setView(dialogLayout)
            builder.show()
    }

    /*fun afterWrite(){
        commuData.clear()
        *//*grid.adapter.notifyDataSetChanged()*//*
        readFirestore()
    }*/
}
