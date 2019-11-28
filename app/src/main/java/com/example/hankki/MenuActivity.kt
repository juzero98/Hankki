package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_community.*
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.activity_mypage.*

class MenuActivity  : AppCompatActivity(), MenuFirstFragment.OnMyListener, MenuSecondFragment.OnMyListener, MenuThirdFragment.OnMyListener  {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.addFragment(MenuFirstFragment(), "면류&찌개&김밥")
        adapter.addFragment(MenuSecondFragment(), "덮밥류&비빔밥")
        adapter.addFragment(MenuThirdFragment(), "볶음밥&오므라이스&돈까스")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }

    override fun onReceivedData(data: Any) {
        val intent = Intent(this, DetailActivity::class.java)
        val datas = data        as Array<String>
        intent.putExtra("img", datas[0])
        intent.putExtra("name", datas[1])
        intent.putExtra("price", datas[2])
        intent.putExtra("category", datas[3])
        startActivity(intent)
    }



}