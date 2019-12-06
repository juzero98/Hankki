package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list.view.*

// GiveReviewActivity GridView inflate해주는 Adapter
class GiveReviewAdapter : BaseAdapter {
    private val ctx: Context?
    private val data: ArrayList<ReadOrderMenuData>
    private val db = FirebaseFirestore.getInstance()
    private var userId = "1"


    constructor(_ctx: Context?, _data: ArrayList<ReadOrderMenuData>) {
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

        val img = view.img
        val menu = view.menu
        val star = view.star
        val review = view.review

        val d1 = data[position]

        // 메뉴 이름 띄우기
        menu.text = d1.menu

        // DB 'menu'에서 메뉴 이름으로 이미지 불러오기
        db.collection("menu")
            .whereEqualTo("name", d1.menu)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val imga = document.get("img").toString()

                    Glide.with(view)
                        .load(imga)
                        .into(img)

                }
            }

        // DB 'reviews'에서 사용자 id와 메뉴 이름으로 별점과 리뷰 불러오기
        db.collection("reviews")
            .whereEqualTo("id", d1.id)
            .whereEqualTo("menu", d1.menu)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val stara = document.get("star").toString()
                    val stars = stara.toFloat()
                    val reviewa = document.get("review").toString()

                    review.text = reviewa
                    star.rating = stars!!

                }


            }
        return view
    }
}