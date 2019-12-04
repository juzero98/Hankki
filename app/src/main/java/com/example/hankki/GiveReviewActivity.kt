package com.example.hankki


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_mypage.*
import kotlinx.android.synthetic.main.activity_mypage.grid

class GiveReviewActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val readOrderMenuData = ArrayList<ReadOrderMenuData>()
    //  private val readReviewData = ArrayList<ReadReviewData>()
    private var readSucess = false
    private val datas = arrayOfNulls<String>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_givereview)

        val prefs = getSharedPreferences("user", 0)
        var userId = prefs.getString("id", "")
        readOrderMenu(userId)


        swipe.setOnRefreshListener {
            afterWrite()

            swipe.isRefreshing = false
        }

    }

    fun afterWrite(){
        val userIdd = intent.getStringExtra("id")
        readOrderMenuData.clear()
        readOrderMenu(userIdd)
    }


    //orders DB에서 내가 먹은 메뉴 불러오기
    private fun readOrderMenu(userId: String?) {
        val userIdd = intent.getStringExtra("id")
        db.collection("orders")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    datas[0] = document.get("menu").toString()//로그인한 사용자가 주문한 메뉴
                    datas[1] = (0.0).toString()//star
                    datas[2] = null//review
                    //   datas[3] = //id
                    readOrderMenuData.add(ReadOrderMenuData(datas[0],datas[1]!!.toFloat(),datas[2],userIdd))
                }


                upload()
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }

    fun upload() {
        val mGrid = grid
        //  val mAdapter = GiveReviewAdapter(this, readOrderMenuData,userId!!)
        val mAdapter = GiveReviewAdapter(this, readOrderMenuData)
        mGrid.adapter = mAdapter
        mGrid.setOnItemClickListener{ parent, view, position, id ->
            // uploadMenu(readOrderMenuData[position].menu)
            val intent = Intent(this, WriteReview::class.java)
            intent.putExtra("menu",readOrderMenuData[position].menu)
            startActivity(intent)

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