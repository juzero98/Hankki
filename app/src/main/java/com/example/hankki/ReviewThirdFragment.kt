package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.*

// 평점 보기에서 세번째 탭에 해당되는 fragment
class ReviewThirdFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val reviewData = ArrayList<SeeReviewData>()
    private var readSucess  = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_review_thirdfragment, container, false)!!
        read()
        return view
    }

    //menu 컬렉션 중 "볶음밥&오므라이스&돈까스" category 인 것만 get
    private fun read() {
        db.collection("menu")
            .whereEqualTo("category", "볶음밥&오므라이스&돈까스")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()

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

    // OneLineReivew로 메뉴
    private fun oneLineReview(menu: String?) {
        val intent = Intent(context, OneLineReview::class.java)
        intent.putExtra("menu",menu)
        startActivity(intent)

    }


}