package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_review.view.*
import java.util.ArrayList

// SeeReviewActivity의 각 fragment에 inflate해주는 adapter
class ReviewAdapter : BaseAdapter {
    private val ctx: Context?
    private val data: ArrayList<SeeReviewData>
    private val db = FirebaseFirestore.getInstance()
    private val starsArray = emptyArray<Float>()
    var i = 0
    var sum = 0.0
    var avg = 0.0

    constructor(_ctx: Context?, _data: ArrayList<SeeReviewData>) {
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
        view = inflater.inflate(R.layout.list_review, parent, false)

        val image = view.img
        val name = view.name
        val price = view.price
        val star = view.star


        val m = data[position]

        Glide.with(view)
            .load(m.img)
            .into(image)

        name.text = m.name

        //reviews 컬렉션에서 각 name과 같은 menu 필드를 get
        db.collection("reviews")
            .whereEqualTo("menu", m.name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val stara = document.get("star").toString()
                    val stars = stara.toFloat()

                    //별점 평균 계산하는 코드
                    sum += stars
                    i++
                    avg = sum/i
                    star.rating = avg.toFloat()

                }
                price.text = (m.price.toString() + "원")
            }

        return view
    }
}
