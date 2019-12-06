package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_oneline.view.*

// OneLineReview의 list에 평점을 inflate 해주는 어댑터
class OneLineAdapter : BaseAdapter {
    private val ctx: Context?
    private val data: ArrayList<OnelineData>
    private val db = FirebaseFirestore.getInstance()


    constructor(_ctx: Context?, _data: ArrayList<OnelineData>) {
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
        view = inflater.inflate(R.layout.list_oneline, parent, false)

        val id = view.oneLineId
        val review = view.oneLineReview
        val menuName = view.oneLineMenuName

        val m = data[position]

        menuName.text = m.menuname

        review.text = m.review
        id.text = m.id
        return view
    }
}