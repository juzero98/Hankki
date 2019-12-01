package com.example.hankki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
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
                }
            }

            for(data : Cart in datas) {
                totalPrice += data.price!! * data.amount!!
            }
        }

        totalPriceView.text = totalPrice.toString()


        yesBtn.setOnClickListener {

        }

        noBtn.setOnClickListener {
            this.finish()
        }


    }
}