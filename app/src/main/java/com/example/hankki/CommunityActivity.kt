package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*

class CommunityActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val commuData = ArrayList<MyCommunity>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        val id = intent.getStringExtra("id")

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

        readFirestore()
    }

    fun afterWrite(){
        commuData.clear()
        readFirestore()
    }

    fun readFirestore(){ //db 읽어와
        db.collection("board")
            .orderBy("count")
            .addSnapshotListener{snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            var title = dc.document.get("title").toString()
                            commuData.add(MyCommunity(title))
                        }
                    }
                }
                upload()
            }

    }

    fun upload(){ //gridview에 upload
        val mGrid = grid
        val reversedCommuData = commuData.reversed() as MutableList<MyCommunity>
        val mAdapter = CommunityAdapter(this, reversedCommuData)
        mGrid?.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        mGrid.setOnItemClickListener{ parent, view, position, id ->
            showDialog(reversedCommuData[position].titleId)
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
    // 상단 바에 장바구니 메뉴달기
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }
}
