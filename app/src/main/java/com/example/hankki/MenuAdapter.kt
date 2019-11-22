package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.util.ArrayList

class MenuAdapter constructor(_ctx : Context?, _data : ArrayList<Menu>) : BaseAdapter() {
    private val ctx : Context?
    private val data : ArrayList<Menu>
    init {
        ctx = _ctx
        data = _data
    }
    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemId(position: Int): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(position: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        if (view == null) {
            val inflater = LayoutInflater.from(ctx)
            view = inflater.inflate(R.layout.list_menu, parent, false)
        }
        val image =  view?.findViewById(R.id.img) as ImageView
        val name = view?.findViewById(R.id.name) as TextView
        val price = view?.findViewById(R.id.price) as TextView

        val m = data.get(position)
        Glide.with(view)
            .load(m.img)
            .into(image)

        name.setText(m.name)
        price.setText(m.price.toString() + "원")

        return view
    }


}