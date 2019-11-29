package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
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

       /* grid.setOnItemClickListener{
            val intent  = Intent(this, showWriteActivity::class.java)
            startActivity(intent)
        }*/


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
            builder.setTitle("showWrite")
            val dialogLayout = inflater.inflate(R.layout.writeshow_dialog,null)
            val dialogTitle = dialogLayout.findViewById<TextView>(R.id.dialogTitle)
            val dialogContent = dialogLayout.findViewById<TextView>(R.id.dialogContent)
            val dialogUser = dialogLayout.findViewById<TextView>(R.id.dialogUser)

            builder.setView(dialogLayout)

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

            builder.show()

    }
}
