package com.example.hankki

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_community.*


class CommunityActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        setSupportActionBar(toolbar)

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.addFragment(CommunityFirstFragment(), "건의사항")
        adapter.addFragment(CommunitySecondFragment(), "학식정보")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }


}