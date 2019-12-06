package com.example.hankki


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_mypage.grid

// 리뷰와 별점을 주기 위해 사용자가 먹은 메뉴를 띄워주는 Activity
class GiveReviewActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val readOrderMenuData = ArrayList<ReadOrderMenuData>()
    private val datas = arrayOfNulls<String>(3)
    private var userId : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_givereview)

        val prefs = getSharedPreferences("user", 0)
        userId = prefs.getString("id", "")!!

        readOrderMenu(userId)

        // swipe 했을 시 게시판 새로 고침
        swipe.setOnRefreshListener {
            afterWrite()

            swipe.isRefreshing = false
        }

    }

    // swipe 했을 시 새로 고침되는 함수
    private fun afterWrite(){
        readOrderMenuData.clear()
        readOrderMenu(userId)
    }


    // DB 'orders'에서 사용자가 먹은 메뉴 불러오기
    private fun readOrderMenu(userId: String?) {
        db.collection("orders")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    datas[0] = document.get("menu").toString()  // 사용자가 먹은 메뉴
                    datas[1] = (0.0).toString() // 별점 -> 0.0으로 초기화
                    datas[2] = null // review -> null로 초기화
                    readOrderMenuData.add(ReadOrderMenuData(datas[0],datas[1]!!.toFloat(),datas[2],userId))
                }
                upload()
            }
            .addOnFailureListener { }
    }

    // GridView에 inflate 하기
    private fun upload() {
        val mGrid = grid
        val mAdapter = GiveReviewAdapter(this, readOrderMenuData)
        mGrid.adapter = mAdapter
        // 각 메뉴 눌렀을 시 별점과 리뷰 주는 activity 띄우기
        mGrid.setOnItemClickListener{ parent, view, position, id ->
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