package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.list_menu.view.*
import java.util.ArrayList

// 각 MenuFragment마다 GridView에 inflate해주는 Adapter
class MenuAdapter : BaseAdapter {
    private val ctx: Context?
    private val data: ArrayList<Menu>

    constructor(_ctx: Context?, _data: ArrayList<Menu>) {
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
        view = inflater.inflate(R.layout.list_menu, parent, false)

        val image = view.img
        val name = view.name
        val price = view.price

        val m = data[position]

        // 이미지 띄워주기
        Glide.with(view)
            .load(m.img)
            .into(image)

        name.text = m.name
        price.text = (m.price.toString() + "원")

        return view
    }


}