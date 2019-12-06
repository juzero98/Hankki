package com.example.hankki

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_menu_firstfragment.grid

// "볶음밥&오므라이스&돈까스" 카테고리의 Fragment
class MenuThirdFragment : Fragment() {
    private val db = FirebaseFirestore.getInstance()
    private val menuData = ArrayList<Menu>()
    // 중복 띄우기 방지, 한 번 읽으면 true로 변경
    private var readSucess = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_menu_firstfragment, container, false)!!
        read()
        return view
    }

    // DB에서 "볶음밥&오므라이스&돈까스" 카테고리의 메뉴 불러오기
    // 불러온 data를 menuData에 Menu형으로 저장
    private fun read() {
        db.collection("menu")
            .whereEqualTo("category", "볶음밥&오므라이스&돈까스")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val img = document.get("img").toString()
                    val name = document.get("name").toString()
                    val price = document.get("price").toString()
                    if (!readSucess) {
                        menuData.add(Menu(img, name, Integer.parseInt(price)))
                    }
                }
                upload()
                readSucess = true

            }
            .addOnFailureListener {
            }
    }

    // menuData에 저장된 내용 Adapter로 inflate
    private fun upload() {
        val mGrid = grid
        val mAdapter = MenuAdapter(this.activity, menuData)
        mGrid.adapter = mAdapter
        // 각 메뉴별 클릭 시 이벤트 달기
        mGrid.setOnItemClickListener { parent, view, position, id ->
            showDetail(menuData[position].name)
        }
    }


    // 각 메뉴 클릭 시 부모 액티비티인 MenuActivity의 onReceivedData 함수 실행
    private fun showDetail(name: String?) {
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
            .addOnFailureListener {
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