package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_menu.*

data class Menu(var img: String, var name: String, var price: Int)

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
        val datas = data as Array<String>
        intent.putExtra("img", datas[0])
        intent.putExtra("name", datas[1])
        intent.putExtra("price", datas[2])
        intent.putExtra("category", datas[3])
        startActivity(intent)
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