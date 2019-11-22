package com.example.hankki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageView = img
        val nameView = name
        val priceView = price
        val categoryView = category

        val intent = intent
        val img = intent.extras!!.getString("img")
        Glide.with(this)
            .load(img)
            .into(imageView)

        val name = intent.extras!!.getString("name")
        nameView.text = name

        val price = intent.extras!!.getString("price")
        priceView.text = (price.toString() + "원")

        val category = intent.extras!!.getString("category")
        categoryView.text = category

        val orderNumView = orderNum
        val plusButton = plusBtn
        val minusButton = minusBtn

        var number = 1
        plusButton.setOnClickListener {
            number =Integer.parseInt(orderNumView.text.toString())
            if(number > 0)
                orderNumView.text = (number + 1).toString()
        }
        minusButton.setOnClickListener {
            number =Integer.parseInt(orderNumView.text.toString())
            if(number > 1)
            orderNumView.text = (number - 1).toString()
        }

    }
}