package com.example.hankki


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*

// MyPage에 들어갈 data를 나타내는 클래스
data class MyPage(var menu: String, var star: Float, var review: String)

// 사용자의 id, 잔액, 평점과 리뷰를 나타내는 Activity
class MyPageActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val reviewData = ArrayList<MyPage>()
    // 중복 띄우기 방지, 한 번 읽으면 true로 변경
    private var readSucess = false
    private val datas = arrayOfNulls<String>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val prefs = getSharedPreferences("user", 0)
        var userCash = prefs.getInt("cash", 0)
        var userId = prefs.getString("id", "")

        // DB 'reviews'에서 data 불러오기
        readReview()

        // DB 'users'에서 data 불러오기
        readUser(userId)

        // 충전 버튼 클릭 시
        cash_charge.setOnClickListener {
            val userId = intent.getStringExtra("id")
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater

            val dialogLayout = inflater.inflate(R.layout.charge_dialog, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.dialogEt)

            builder.setTitle("cashCharge")
            builder.setView(dialogLayout)
            // OK 버튼 클릭 시
            // 입력한 비밀번호가 관리자 비밀번호('1234')와 일치하면 1000원 충전
            builder.setPositiveButton("OK") { dialogInterface, i ->
                // 관리자 비밀번호와 일치할 시 DB 'users'의 cash 필드를 + 1000
                if (editText.text.toString().equals("1234")) {
                    db.collection("users")
                        .document(userId)
                        .update("cash", FieldValue.increment(1000))

                    // DB 'users'의 update된 data를 불러와 띄우기
                    readUser(userId)
                    Toast.makeText(
                        applicationContext,
                        "충전이 완료되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else // 비밀번호가 일치하지 않을 시
                    Toast.makeText(
                        applicationContext,
                        "비밀번호가 맞지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

            }

            builder.show()

        }

    }

    // DB 'users'에서 사용자 id를 이용해 사용자 data 불러와서 띄워주기
    private fun readUser(userId: String?) {
        db.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    datas[0] = document.get("id").toString()
                    datas[1] = document.get("name").toString()
                    datas[2] = document.get("cash").toString()
                }
                user_name.text = datas[1]//.toString()
                user_cash.text = datas[2]//.toString()
            }
            .addOnFailureListener {
            }
    }

    // DB 'review'에서 사용자 id를 이용해 평점과 리뷰 data 불러와서 띄워주기
    private fun readReview() {
        val userId = intent.getStringExtra("id")
        db.collection("reviews")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val star = document.get("star").toString()
                    val review = document.get("review").toString()
                    val menu = document.get("menu").toString()
                    if (!readSucess) {
                        reviewData.add(MyPage(menu, star.toFloat(), review))
                    }
                }
                upload()
                readSucess = true

            }
            .addOnFailureListener {
            }
    }

    // GridView에 inflate 해주기
    private fun upload() {
        val mGrid = grid
        val mAdapter = MyAdapter(this, reviewData)
        mGrid.adapter = mAdapter

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