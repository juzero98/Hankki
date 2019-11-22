package com.example.hankki

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

class MenuFirstFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val menuData = ArrayList<Menu>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view : View = inflater.inflate(R.layout.fragment_menu_firstfragment, container, false)!!
        read()
        return view
    }

    fun read() {
        db.collection("menu")
            .whereEqualTo("category", "면류&찌개&김밥")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()
                    menuData.add(Menu(img, name, Integer.parseInt(price)))
                }
                upload()
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }
    fun upload() {
        val mGrid = grid
        val mAdapter = MenuAdapter(this.activity, menuData)
        mGrid.setAdapter(mAdapter)
        /*mGrid.setOnItemClickListener{ parent, view, position, id ->
            //var position = position

            //itemView(mData.get(position).name)
            //Toast.makeText(MainActivity.this, mData.get(position).name + " 선택!", Toast.LENGTH_SHORT).show();
        }*/
    }


}