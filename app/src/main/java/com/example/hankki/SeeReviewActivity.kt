
package com.example.hankki

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_seereview.*

class SeeReviewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seereview)

        val adapter = MyPagerAdapter(supportFragmentManager)
        adapter.addFragment(ReviewFirstFragment(), "면류&찌개&김밥")
        adapter.addFragment(ReviewSecondFragment(), "덮밥류&비빔밥")
        adapter.addFragment(ReviewThirdFragment(), "볶음밥&오므라이스&돈까스")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)



    }


}
