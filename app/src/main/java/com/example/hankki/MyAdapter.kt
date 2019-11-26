package com.example.hankki

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list.view.*

class MyAdapter : BaseAdapter {
    private val ctx: Context?
    private val data: ArrayList<MyPage>

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

        Glide.with(view)
            .load(m.img)
            .into(image)

        menu.text = m.menu
        review.text = m.review
        star.rating = m.star!!
        return view
    }


}