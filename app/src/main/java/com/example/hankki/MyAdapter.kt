package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list.view.*

// MyPage의 GridView에 inflate 해주기
class MyAdapter : BaseAdapter {
    private val ctx: Context?
    private val data: ArrayList<MyPage>
    private val db = FirebaseFirestore.getInstance()

    constructor(_ctx: Context?, _data: ArrayList<MyPage>) {
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
        view = inflater.inflate(R.layout.list, parent, false)

        val menu = view.menu
        val star = view.star
        val review = view.review
        val image = view.img

        val m = data[position]

        menu.text = m.menu

        // DB 'menu'에서 메뉴 이름으로 이미지,평점,리뷰 불러오기
        db.collection("menu")
            .whereEqualTo("name", m.menu)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val imga = document.get("img").toString()
                    // 이미지 불러오기
                    Glide.with(view)
                        .load(imga)
                        .into(image)

                }

                review.text = m.review
                star.rating = m.star!!

            }
        return view
    }
}