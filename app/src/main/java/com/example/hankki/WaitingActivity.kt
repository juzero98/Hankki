package com.example.hankki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_waiting.*
import kotlinx.android.synthetic.main.activity_waiting.myCashView
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Parcelable


class WaitingActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var id : String= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val db = FirebaseFirestore.getInstance()
        val intent = intent

        val menus : ArrayList<String> = intent.getStringArrayListExtra("menus")
        val amounts : ArrayList<Int> = intent.getIntegerArrayListExtra("amounts")
        val totalPrice = intent.getIntExtra("totalPrice",0)
        val myCash = intent.getStringExtra("myCash")
        val prefs = getSharedPreferences("user", 0)
        id = prefs.getString("id", "").toString()

        var count = 1
        db.collection("orders").orderBy("orderNum")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val orderNum = document.get("orderNum").toString()
                    if(count == Integer.parseInt(orderNum)){
                        count++
                    }
                }
                val range = menus.size - 1
                for(i in 0..range){
                    val order = Order(count, id, menus.get(i), false, amounts.get(i))
                    db.collection("orders").document(order.orderNum.toString()+order.menu).set(order)
                }
                orderNum.text = count.toString()
            }


        calcMyCash(totalPrice, myCash)

        closeBtn.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
        }


    }

    // 내 잔액에서 차감하는 함수
    fun calcMyCash(totalPrice : Int, myCash : String) {
        var cash = Integer.parseInt(myCash)
        cash = cash.minus(totalPrice)
       db.collection("users")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    document.reference.update("cash", cash)
                }
                myCashView.text = cash.toString()
            }
    }


}