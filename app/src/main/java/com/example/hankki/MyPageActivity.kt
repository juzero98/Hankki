package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mypage.*

class MyPageActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    //    private val userData = ArrayList<User>()
    private val reviewData = ArrayList<MyPage>()
    private var readSucess = false
    private val datas = arrayOfNulls<String>(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mypage)

        val prefs = getSharedPreferences("user", 0)
        var userCash = prefs.getInt("cash", 0)
        var userId = prefs.getString("id", "")

        readReview()
        readUser(userId)
        cash_charge.setOnClickListener {
            val userId = intent.getStringExtra("id")
            // userCash = userCash + 1000
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            builder.setTitle("cashCharge")
            val dialogLayout = inflater.inflate(R.layout.charge_dialog, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.dialogEt)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                if (editText.text.toString().equals("1234")) {
                    db.collection("users")
                        .document(userId)
                        .update("cash", FieldValue.increment(1000))

                    Toast.makeText(
                        applicationContext,
                        "충전이 완료되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else
                    Toast.makeText(
                        applicationContext,
                        "비밀번호가 맞지 않습니다.",
                        Toast.LENGTH_SHORT
                    ).show()

            }
            readUser(userId)
            builder.show()




        }


    }

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
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }
    fun readReview() {
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
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }


    fun upload() {
        val mGrid = grid
        val mAdapter = MyAdapter(this, reviewData)
        mGrid.adapter = mAdapter

    }

}