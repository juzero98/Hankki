package com.example.hankki

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

// 메뉴에서 각 메뉴를 눌렀을 때 자세한 정보 뜨는 Activity
class DetailActivity : AppCompatActivity() {
    var price = 0
    var amount = 0
    var totalPrice = 0
    private val projection = arrayOf("name", "price", "amount")

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

        price = Integer.parseInt(intent.extras!!.getString("price")!!)
        priceView.text = (price.toString() + "원")
        totalPriceView.text = price.toString()

        // 총 주문 수량
        amount = 1

        // 총 금액
        totalPrice  = price

        // 수량 +/- 버튼 클릭 이벤트 (총 주문 수량 변경 및 총 금액 변경)
        plusBtn.setOnClickListener {
            amount = Integer.parseInt(amountView.text.toString())
            if(amount > 0) {
                amount += 1
                amountView.text = amount.toString()
            }
            totalPrice = price * amount
            totalPriceView.text = totalPrice.toString()
        }
        minusBtn.setOnClickListener {
            amount =Integer.parseInt(amountView.text.toString())
            if(amount > 1) {
                amount -= 1
                amountView.text = amount.toString()
            }
            totalPrice = price * amount
            totalPriceView.text = totalPrice.toString()
        }

        // 장바구니 담기 버튼 클릭 이벤트
        cartBtn.setOnClickListener {
            // 장바구니에 담을 목록 SQLite에 저장
            val helper = CartDBHelper(this)
            val db = helper.writableDatabase

            val cur = db.query("cart", projection, "name=?", Array<String>(1){name!!}, null, null, null)

            // 장바구니에 이미 담긴 메뉴일 때
            if (cur.count != 0) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("이미 담긴 메뉴")
                builder.setMessage("이미 담긴 메뉴입니다.")
                builder.setPositiveButton("닫기", DialogInterface.OnClickListener { dialog, which ->
                })
                builder.create().show()
            }

            // 장바구니에 담기지 않은 메뉴일 때
            else {
                val value = ContentValues()
                value.put("name", name)
                value.put("price", price)
                value.put("amount", amount)
                db.insert("cart", null, value)

                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }

            helper.close()
        }



        // 바로 주문 버튼 클릭 이벤트
        orderBtn.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("price", price)
            intent.putExtra("amount", amount)
            startActivityForResult(intent, 0)
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