package com.example.hankki

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_select.*

// 로그인 후 4개의 카테고리 중에 선택하는 액티비티
class SelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select)

        val id = intent.getStringExtra("id")
        val cash = intent.getStringExtra("cash")

        //각 버튼을 누르면 해당 액티비티로 이동을 한다

        menuBtn.setOnClickListener{
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        communityBtn.setOnClickListener{
            val intent = Intent(this, CommunityActivity::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        scoreBtn.setOnClickListener{
            val intent1 = Intent(this, RatingActivity::class.java)
            intent1.putExtra("id", id)
            startActivity(intent1)
        }

        myPageBtn.setOnClickListener{

            val intent1 = Intent(this, MyPageActivity::class.java)

            intent1.putExtra("id", id)
            startActivity(intent1)
        }


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