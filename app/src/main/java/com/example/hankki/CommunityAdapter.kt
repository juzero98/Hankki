package com.example.hankki

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.community_list.view.*

class CommunityAdapter : BaseAdapter {

    private val db = FirebaseFirestore.getInstance()
    private val ctx: Context?
    private var data = mutableListOf<MyCommunity>()

    constructor(_ctx: Context?, _data: MutableList<MyCommunity>) {
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
        view = inflater.inflate(R.layout.community_list, parent, false)

        val titleId = view.titleId
        val m = data[position]
        titleId.text = m.titleId



        return view
    }
}