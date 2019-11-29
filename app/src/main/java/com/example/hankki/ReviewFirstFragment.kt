package com.example.hankki

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.*
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import android.R.attr.fragment



//class ReviewFirstFragment : Fragment() {
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

    private fun read() {
        db.collection("menu")
            .whereEqualTo("category", "면류&찌개&김밥")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()
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
            // showDetail(menuData[position].name)
        }
    }

    private fun oneLineReview(name : String?) {

        val dialogFragment = fragment as DialogFragment
        dialogFragment.dismiss()

        val alert: AlertDialog? = null
        val build = AlertDialog.Builder(this.activity!!)
        val inflater = activity!!.layoutInflater
        build.setTitle("One-line review")
        val dialogLayout = inflater.inflate(R.layout.oneline_dialog, null)
        // val editText = dialogLayout.findViewById<EditText>(R.id.dialogEt)
        build.setView(dialogLayout)
        read1(name)
        build.setPositiveButton("OK") { dialogInterface, i ->

        }
        build.show()

    }

    private fun read1(menuname : String?) {
        db.collection("menu")
            .whereEqualTo("menu", menuname)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val menuname = document.get("name").toString()
                    if (!readSucess) {
                        // reviewData.add(SeeReviewData(img, name, price, star.toFloat()))
                        onelineData.add(OnelineData(menuname))
                    }

                }
                upload1()
                readSucess = true

            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }

    fun upload1() {
        val mGrid = grid
        val mAdapter =
            OneLineAdapter(activity!!,onelineData )
        mGrid.adapter = mAdapter

    }
}