package com.example.hankki

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.*
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.view.*

class ReviewSecondFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val reviewData = ArrayList<SeeReviewData>()
    private var readSucess  = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_review_secondfragment, container, false)!!
        read()
        return view
    }

    private fun read() {
        db.collection("menu")
            .whereEqualTo("category", "덮밥류&비빔밥")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()

                    if (!readSucess) {
                        // reviewData.add(SeeReviewData(img, name, price, star.toFloat()))
                        reviewData.add(SeeReviewData(img, name, price))
                    }

                }
                upload()
                readSucess = true

            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }
    private fun upload() {
        val mGrid = grid
        val mAdapter = ReviewAdapter(this.activity, reviewData)
        mGrid.adapter = mAdapter
        mGrid.setOnItemClickListener{ parent, view, position, id ->
            oneLineReview(reviewData[position].name)
        }
    }

    private fun oneLineReview(menu: String?) {
        val intent = Intent(context, OneLineReview::class.java)
        intent.putExtra("menu",menu)
        startActivity(intent)

    }


}