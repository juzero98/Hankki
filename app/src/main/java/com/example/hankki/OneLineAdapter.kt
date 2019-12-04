package com.example.hankki

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_oneline.view.*

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

      /*  db.collection("reviews")
            .whereEqualTo("menu", m.menuname)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val oneLineId = document.get("id").toString()
                    val oneLineReview = document.get("review").toString()

                    id.text = oneLineId
                    review.text = oneLineReview

                }


            }*/
        review.text = m.review
        id.text = m.id
        return view
    }
}