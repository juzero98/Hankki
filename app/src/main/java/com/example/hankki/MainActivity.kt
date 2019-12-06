package com.example.hankki


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*

// 메인 화면 Activity
class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그인 버튼 클릭 시
        loginBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 클릭 시
        joinBtn.setOnClickListener{
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        // Preference에 저장된 사용자 id 불러오기
        val prefs = getSharedPreferences("user", 0)
        val id : String = prefs.getString("id","").toString()


        // 주문한 음식이 완성되면 알림 띄우기
        // finish 필드로 음식이 완성됐는지 안됐는지 확인
        // finish : 기본값은 false, 음식이 완료되면 true로 변경
        db.collection("orders")
            .whereEqualTo("id", id)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                // DB 'orders'의 내용이 변경되면 바로 알림 띄우는 함수 실행
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.MODIFIED -> {
                            notify(dc.document.get("finish").toString().toBoolean(), dc.document.get("menu").toString(), dc.document.get("orderNum").toString())
                        }
                    }
                }
            }
    }

    // 음식이 완성되면 알림 띄우기
    private fun notify(finish : Boolean, orderedMenu:String , orderedNum:String) {
        // finish가 true면 (관리자 어플에서 완료 버튼을 눌렀을 시)
        if(finish) {
            val notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val channel = NotificationChannel("음식완성", "한성세끼", NotificationManager.IMPORTANCE_HIGH)
                channel.description = "한성세끼"
                channel.enableLights(true)
                channel.lightColor = Color.RED
                channel.enableVibration(true)
                channel.setShowBadge(false)
                notificationManager.createNotificationChannel(channel)
            }

            var pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            val notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            // 주문 번호와 주문한 메뉴 이름 넣어서 알림 띄우기
            var notificationBuilder = NotificationCompat.Builder(this, "음식완성")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_new))
                .setSmallIcon(R.mipmap.ic_launcher_new)
                .setContentTitle("음식 완성")
                .setContentText(orderedNum + "번 손님! " + orderedMenu + " 음식을 수령해주세요")
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)

            notificationManager.notify(0, notificationBuilder.build())

        }
    }

}