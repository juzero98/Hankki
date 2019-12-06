package com.example.hankki

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_order.*

data class Order (var orderNum : Int, var id : String, var menu : String, var finish : Boolean, var amount : Int)

class OrderActivity : AppCompatActivity() {
    val db = FirebaseFirestore.getInstance()
    val menus = arrayListOf<String>()
    val amounts = arrayListOf<Int>()
    var tp = 0
    var myCash = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        getAll()


    }
    fun getAll() {
        val helper = CartDBHelper(this)
        val cartDB = helper.writableDatabase
        val projection = arrayOf("name", "price", "amount")

        val datas = ArrayList<Cart>()

        val intent = intent
        var name = ""
        var price = 0
        var amount = 0
        var totalPrice = 0

        // 바로 주문일 때
        if(callingActivity?.className == "com.example.hankki.DetailActivity") {
            name = (intent.extras!!.getString("name"))!!
            price = intent.extras!!.getInt("price")
            amount = intent.extras!!.getInt("amount")
            totalPrice = price!! * amount!!
            menus.add(name)
            amounts.add(amount)
        }

        // 장바구니 - 주문일 때
        else if(callingActivity?.className == "com.example.hankki.CartActivity") {
            val cur = cartDB.query("cart", projection, null, null, null, null, null)
            if (cur != null) {
                val name_col = cur.getColumnIndex("name")
                val price_col = cur.getColumnIndex("price")
                val amount_col = cur.getColumnIndex("amount")
                while(cur.moveToNext()) {
                    datas.add(Cart(cur.getString(name_col), cur.getInt(price_col), cur.getInt(amount_col)))
                    menus.add(cur.getString(name_col))
                    amounts.add(cur.getInt(amount_col))
                }
            }

            for(data : Cart in datas) {
                totalPrice += data.price!! * data.amount!!
            }
        }

        tp = totalPrice
        totalPriceView.text = totalPrice.toString()
        getMyCash()
    }
    // 내 잔액 불러오는 함수
    fun getMyCash() {
        val prefs = getSharedPreferences("user", 0)
        val id : String = prefs.getString("id","").toString()
        db.collection("users")
            .whereEqualTo("id", id)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    myCash = document.get("cash").toString()
                }
                myCashView.text = myCash
            }

        setListener()
    }

    fun setListener() {
        yesBtn.setOnClickListener {
            val intent = Intent(this, WaitingActivity::class.java)
            intent.putExtra("menus", menus)
            intent.putExtra("amounts", amounts)
            intent.putExtra("totalPrice", tp)
            intent.putExtra("myCash", myCash)
            startActivity(intent)
            this.finish()
        }

        noBtn.setOnClickListener {
            this.finish()
        }
    }
}