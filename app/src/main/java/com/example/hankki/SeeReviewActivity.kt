
package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_seereview.*

data class SeeReviewData(var img: String, var name: String, var price: String)

// 별점보기를 누르게 되면 뜨는 액티비티
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

    // 상단 바에 장바구니 메뉴달기
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_cart -> {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> {return super.onOptionsItemSelected(item)}
        }
    }

}
