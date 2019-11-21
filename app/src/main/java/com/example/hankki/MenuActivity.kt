package com.example.hankki

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_mypage.*

class MenuActivity  : AppCompatActivity()  {
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.addFragment(MenuFirstFragment(), "면류/찌개/김밥")
        adapter.addFragment(MenuSecondFragment(), "덮밥류/비빔밥")
        adapter.addFragment(MenuThirdFragment(), "볶음밥/오므라이스/돈까스")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)


    }

   /* fun upload() {
        val userId = intent.getStringExtra("id")
        db.collection("users")
            .whereEqualTo("id", userId)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    datas[0] = document.get("id").toString()
                    datas[1] = document.get("name").toString()
                    datas[2] = document.get("cash").toString()
//                    datas[3] = document.get("pw").toString()
                }
                user_name.text = datas[1]//.toString()
                user_cash.text = datas[2]//.toString()
            }
            .addOnFailureListener { exception ->
                Log.w("", "Error getting documents: ", exception)
            }
    }*/

}