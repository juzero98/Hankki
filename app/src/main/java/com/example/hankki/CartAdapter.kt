package com.example.hankki

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_cart.view.*
import java.util.ArrayList

// CartActivity의 ListView에 inflate해주는 Adapter
class CartAdapter : BaseAdapter {
    private val db = FirebaseFirestore.getInstance()
    private val ctx: Context?
    private val data: ArrayList<Cart>
    private val projection = arrayOf("name", "price", "amount")

    private var price = 0
    private var amount = 0
    private var totalPriceOfMenu = 0

    constructor(_ctx: Context?, _data: ArrayList<Cart>) {
        ctx = _ctx
        data = _data
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        val inflater = LayoutInflater.from(ctx)
        view = inflater.inflate(R.layout.list_cart, parent, false)


        val imgView = view.img
        val nameView = view.name
        val priceOfMenuView = view.priceOfMenu
        val priceView = view.price
        val amountView = view.amountView
        val numView = view.num

        val m = data[position]
        var img : String = ""

        // 해당 메뉴의 img 불러와서 띄우는 코드
        db.collection("menu")
            .whereEqualTo("name", m.name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    img = document.get("img").toString()
                }
                Glide.with(view)
                    .load(img)
                    .into(imgView)

            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }

        // 메뉴 별 총 가격
        totalPriceOfMenu = m.price!! * m.amount!!
        //(ctx as CartActivity).setTotalPrice(ctx.getTotalPrice() + totalPriceOfMenu)

        // 이름, 총 가격, 수량 띄우는 코드
        nameView.text = m.name
        priceOfMenuView.text = m.price.toString()
        priceView.text = (totalPriceOfMenu.toString() + "원")
        amountView.text = m.amount.toString()
        numView.text = m.amount.toString()

        // - 버튼 클릭 시
        view.minusBtn.setOnClickListener {
            change(view, m, "minus");
        }
        // + 버튼 클릭 시
        view.plusBtn.setOnClickListener {
            change(view, m, "plus");
        }
        // 삭제 버튼 클릭 시
        view.deleteBtn.setOnClickListener {
            delete(view, m)
        }

        return view
    }

    // 장바구니에서 +/- 눌렀을 때 DB내용과 Text 내용이 변하는 함수
    fun change(view : View, menu : Cart, option : String) {
        val helper = CartDBHelper(this.ctx!!)
        val cartDB = helper.writableDatabase
        var values : ContentValues = ContentValues()

        var name = menu.name
        price = menu.price!!

        // DB에 저장된 값(amount) 불러오기
        val cur = cartDB.query("cart", projection, "name=?", Array<String>(1){name!!}, null, null, null)
        if (cur != null) {
            val amount_col = cur.getColumnIndex("amount")
            while(cur.moveToNext()) {
                amount = cur.getInt(amount_col)
            }
        }

        // - 버튼 눌렸을 때
        if(option == "minus") {
            // 수량이 1 미만으로 내려가지 않도록
            if(amount == 1) {
                return
            }
            else {
                // 수량 1 감소
                amount = amount?.minus(1)
                totalPriceOfMenu = price!! * amount!!

                // SQLite DB 내용 수정
                values.put("amount", amount)
                cartDB.update("cart", values, "name=?", Array<String>(1) { name!! })
                cartDB.close()

                // TextView 내용 수정
                view.priceOfMenu.text = price.toString()
                view.price.text = (totalPriceOfMenu.toString() + "원")
                view.amountView.text = amount.toString()
                view.num.text = amount.toString()

                (ctx as CartActivity).setTotalPrice(ctx.getTotalPrice() - price)
            }
        }

        // + 버튼 눌렸을 때
        else if(option == "plus") {
            amount = amount?.plus(1)
            totalPriceOfMenu = price!! * amount!!

            // SQLite DB 내용 수정
            values.put("amount",amount)
            cartDB.update("cart", values, "name=?", Array<String>(1){name!!})
            cartDB.close()

            // TextView 내용 수정
            view.priceOfMenu.text = price.toString()
            view.price.text = (totalPriceOfMenu.toString() + "원")
            view.amountView.text = amount.toString()
            view.num.text = amount.toString()

            (ctx as CartActivity).setTotalPrice(ctx.getTotalPrice() + price)
        }


    }

    // 삭제 버튼 눌렀을 때 DB내용과 TextView 지우는 함수
    fun delete(view : View, menu : Cart) {
        val helper = CartDBHelper(this.ctx!!)
        val cartDB = helper.writableDatabase

        val name = menu.name
        var amount = 0
        val cur = cartDB.query("cart", projection, "name=?", Array<String>(1){name!!}, null, null, null)
        if (cur != null) {
            val amount_col = cur.getColumnIndex("amount")
            while(cur.moveToNext()) {
                amount = cur.getInt(amount_col)
            }
        }
        cartDB.delete("cart", "name=?", Array<String>(1){name!!})
        cartDB.close()

        totalPriceOfMenu = menu.price!! * amount

        (ctx as CartActivity).setTotalPrice(ctx.getTotalPrice()-totalPriceOfMenu)
        (ctx as CartActivity).delete()

    }

}