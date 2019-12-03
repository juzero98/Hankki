package com.example.hankki

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cart.*

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
            //startActivity(intent)
        }

        swipe.setOnRefreshListener {
            afterIn()
            swipe.isRefreshing = false
        }
    }

    private fun afterIn(){
        cartData.clear()
        val helper = CartDBHelper(this)
        read(helper)
        /*mListView = listView
        val mAdapter = CartAdapter(this, cartData)
        mListView?.adapter = mAdapter
        mAdapter.notifyDataSetChanged()*/
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
        mListView = listView
        val mAdapter = CartAdapter(this, cartData)
        mListView?.adapter = mAdapter

        for(data : Cart in cartData) {
            totalPrice += data.price!!
        }
        setTotalPrice(totalPrice)
    }

    // 장바구니 총 금액 계산
    public fun setTotalPrice(tp : Int) {
        totalPrice = tp
        totalPriceView.text = totalPrice.toString()
        mListView?.deferNotifyDataSetChanged()
    }

    public fun getTotalPrice() : Int {
        return totalPrice
    }
    /*override public fun onResume() {
        super.onResume()

    }*/

   /* // 상단 바에 장바구니 메뉴달기
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
    }*/
}