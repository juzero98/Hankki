package com.example.hankki

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val intent = intent
        val img = intent.extras!!.getString("img")
        Glide.with(this)
            .load(img)
            .into(imgView)

        val name = intent.extras!!.getString("name").toString()
        nameView.text = name

        val price = intent.extras!!.getString("price").toString()
        priceView.text = (price + "원")
        totalPriceView.text = price

        // 총 주문 수량
        var amount = 1

        // 총 금액
        var totalPrice  = Integer.parseInt(price)

        // 수량 +/- 버튼 클릭 이벤트 (총 주문 수량 변경 및 총 금액 변경)
        plusBtn.setOnClickListener {
            amount = Integer.parseInt(amountView.text.toString())
            if(amount > 0) {
                amount += 1
                amountView.text = amount.toString()
            }
            totalPrice = Integer.parseInt(price) * amount
            totalPriceView.text = totalPrice.toString()
        }
        minusBtn.setOnClickListener {
            amount =Integer.parseInt(amountView.text.toString())
            if(amount > 1) {
                amount -= 1
                amountView.text = amount.toString()
            }
            totalPrice = Integer.parseInt(price) * amount
            totalPriceView.text = totalPrice.toString()
        }

        // 장바구니 담기 버튼 클릭 이벤트
        cartBtn.setOnClickListener {
            // 장바구니에 담을 목록 SQLite에 저장
            val helper = CartDBHelper(this)
            val db = helper.writableDatabase
            val value = ContentValues()
            value.put("name", name)
            value.put("price", Integer.parseInt(price))
            value.put("amount", amount)
            db.insert("cart", null, value)

            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)

            helper.close()
        }

        // 바로 주문 버튼 클릭 이벤트
        orderBtn.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }
    }
}