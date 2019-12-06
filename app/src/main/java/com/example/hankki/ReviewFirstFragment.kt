package com.example.hankki

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.*
import androidx.fragment.app.DialogFragment
import android.content.Intent

// 평점 보기에서 첫번째 탭에 해당되는 fragment
class ReviewFirstFragment : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val reviewData = ArrayList<SeeReviewData>()
    private val onelineData = ArrayList<OnelineData>()
    private val menuData = ArrayList<Menu>()
    private var readSucess  = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_review_firstfragment, container, false)!!
        read()
        return view
    }

    //fragment에 "면류&찌개&김밥" 메뉴와 평점 띄우는 함수
    private fun read() {
        db.collection("menu")
            .whereEqualTo("category", "면류&찌개&김밥")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()
                    val menu = null

                    //성공해야 reviewData에 SeeReviewData 데이터클래스를 add 해준다.
                    if (!readSucess) {
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