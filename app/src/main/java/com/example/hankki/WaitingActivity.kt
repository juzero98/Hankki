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

// 사용자가 주문을 하면 대기번호를 리턴해주는 액티비티
class WaitingActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    var id : String= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val db = FirebaseFirestore.getInstance()
        val intent = intent

        //menu명, 음식 양, 가격, 잔액을 가져온다
        val menus : ArrayList<String> = intent.getStringArrayListExtra("menus")
        val amounts : ArrayList<Int> = intent.getIntegerArrayListExtra("amounts")
        val totalPrice = intent.getIntExtra("totalPrice",0)
        val myCash = intent.getStringExtra("myCash")
        val prefs = getSharedPreferences("user", 0)
        id = prefs.getString("id", "").toString()

        var count = 1
        //orders 컬렉션을 가져와서 orderNum 필드로 정렬해준다.
        db.collection("orders").orderBy("orderNum")
            .get()
            .addOnSuccessListener { documents ->
                //이미 count가 orderNum 필드에 있다면 count++해준다
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
                    //orders 컬렉션을 가져와서 document 이름은 번호와메뉴로 지정해주고 필드는 order 클래스로 넣어준다
                }
                orderNum.text = count.toString()
            }


        calcMyCash(totalPrice, myCash)

        //닫기를 누르면 selectActivity로 돌아간다.
        closeBtn.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
            this.finish()
        }


    }

    // 내 잔액에서 차감하는 함수
    fun calcMyCash(totalPrice : Int, myCash : String) {
        var cash = Integer.parseInt(myCash)
        cash = cash.minus(totalPrice)
        //user 컬렉션을 가져와서 id 필드와 같다면 그 document의 cash를 update시켜준다.
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