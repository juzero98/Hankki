package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cart.*

// 장바구니에 들어갈 data를 나타내는 class
data class Cart(val name: String, val price: Int, val amount: Int)

// 장바구니 Activity
class CartActivity : AppCompatActivity() {
    private val projection = arrayOf("name", "price", "amount")
    private val cartData = ArrayList<Cart>()
    private var mListView : ListView? = null
    private var totalPrice = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val helper = CartDBHelper(this)
        read(helper)

        backBtn.setOnClickListener {
            this.finish()
        }

        orderBtn.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivityForResult(intent, 0)
        }

        swipe.setOnRefreshListener {
            afterIn()
            swipe.isRefreshing = false
        }
    }

    // 장바구니 내용 새로고침
    public fun afterIn(){
        cartData.clear()
        val helper = CartDBHelper(this)
        read(helper)
    }

    // 목록 삭제 시 새로고침
    public fun delete() {
        afterIn()
    }

    // SQLite에서 장바구니(Cart 테이블)에 담긴 목록 불러 와서 ArrayList에 저장
    private fun read(helper: CartDBHelper) {
        val db = helper.readableDatabase
        val cur = db.query("cart", projection, null, null, null, null, null)
        var tp = 0
        if (cur != null) {
            val name_col = cur.getColumnIndex("name")
            val price_col = cur.getColumnIndex("price")
            val amount_col = cur.getColumnIndex("amount")
            while (cur.moveToNext()) {
                val name = cur.getString(name_col)
                val price = cur.getInt(price_col)
                val amount = cur.getInt(amount_col)
                cartData.add(Cart(name, price, amount))
                tp += price * amount
            }
        }
        setTotalPrice(tp)
        upload()
    }

    // ListView에 inflate
    private fun upload() {
        mListView = listView
        val mAdapter = CartAdapter(this, cartData)
        mListView?.adapter = mAdapter
    }

    // 장바구니 총 금액 계산
    public fun setTotalPrice(tp : Int) {
        totalPrice = tp
        totalPriceView.text = totalPrice.toString()
        mListView?.deferNotifyDataSetChanged()
    }

    // 장바구니 총 금액 반환
    public fun getTotalPrice() : Int {
        return totalPrice
    }
}