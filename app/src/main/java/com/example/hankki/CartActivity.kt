package com.example.hankki

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {
    private val projection = arrayOf("name", "price", "amount")
    private val cartData = ArrayList<Cart>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val helper = CartDBHelper(this)
        read(helper)
    }

    // SQLite에서 장바구니(Cart 테이블)에 담긴 목록 불러 와서 ArrayList에 저장
    private fun read(helper: CartDBHelper) {
        val db = helper.readableDatabase
        val cur = db.query("cart", projection, null, null, null, null, null)
        if (cur != null) {
            val name_col = cur.getColumnIndex("name")
            val price_col = cur.getColumnIndex("price")
            val amount_col = cur.getColumnIndex("amount")
            while (cur.moveToNext()) {
                val name = cur.getString(name_col)
                val price = cur.getInt(price_col)
                val amount = cur.getInt(amount_col)
                cartData.add(Cart(name, price, amount))
            }
        }
        upload()
    }

    private fun upload() {
        val mListView = listView
        val mAdapter = CartAdapter(this, cartData)
        mListView.adapter = mAdapter
    }
}