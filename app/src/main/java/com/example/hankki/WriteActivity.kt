package com.example.hankki

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_write.*
import android.app.ListActivity
import android.content.Intent
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.Menu
import android.view.MenuItem

class WriteActivity : AppCompatActivity() {


    private val db = FirebaseFirestore.getInstance()
    private val commuData = ArrayList<MyCommunity>()
    public var context: Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write)

        context = this
        val id = intent.getStringExtra("id")
        var num = 1

        /*val random = Random()
        val randomNum = random.nextInt(100).toString()*/

        writeButton.setOnClickListener{

            /*db.collection("board").document(randomNum).set(board)*/
            var numString = num.toString()

            db.collection("board")
                .orderBy("count")
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents ) {
                        val count = document.get("count").toString()
                        //val numInt = Integer.parseInt(count)
                        if(num == Integer.parseInt(count)){
                            num++
                        }
                    }

                    val writeTitle: String = writeTitle.text.toString()
                    val writeContent: String = writeContent.text.toString()

                    //document 이름이 numString 인 애가 있다면
                    numString = num.toString()
                    val board = Board(writeContent, id, writeTitle, num)
                    db.collection("board").document(numString).set(board)

                    /*readFirestore()*/

                    val community = CommunityActivity()
                    community.compare = 1
                }

            finish()//신기해


        }
    }

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