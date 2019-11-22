package com.example.hankki

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.*
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.grid
import kotlinx.android.synthetic.main.fragment_menu_thirdfragment.*

class MenuThirdFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val menuData = ArrayList<Menu>()
    private var loadSucess  = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu_firstfragment, container, false)!!
        read()
        return view
    }

    private fun read() {
        db.collection("menu")
            .whereEqualTo("category", "볶음밥&오므라이스&돈까스")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()
                    menuData.add(Menu(img, name, Integer.parseInt(price)))
                }
                if(!loadSucess) {
                    upload()
                    loadSucess = true
                }

            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }
    private fun upload() {
        val mGrid = grid
        val mAdapter = MenuAdapter(this.activity, menuData)
        mGrid.adapter = mAdapter
        mGrid.setOnItemClickListener{ parent, view, position, id ->
            showDetail(menuData[position].name)
        }
    }

    private fun showDetail(name : String?) {
        db.collection("menu")
            .whereEqualTo("name", name)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val datas = arrayOfNulls<String>(4)
                    datas[0] = document.get("img")!!.toString()
                    datas[1] = document.get("name")!!.toString()
                    datas[2] = document.get("price")!!.toString()
                    datas[3] = document.get("category")!!.toString()
                    if (mOnMyListener != null)
                        mOnMyListener?.onReceivedData(datas)
                }

            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }
    interface OnMyListener {
        fun onReceivedData(data: Any)
    }

    private var mOnMyListener: OnMyListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (activity != null && activity is OnMyListener) {
            mOnMyListener = activity as OnMyListener?
        }
    }
}