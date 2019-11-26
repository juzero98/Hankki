package com.example.hankki

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


        // 수량 +/- 버튼 (총 주문 수량 변경 및 총 금액 변경)
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
    }
}