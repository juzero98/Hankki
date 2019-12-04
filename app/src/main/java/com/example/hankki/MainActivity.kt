package com.example.hankki

import android.app.Notification
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

class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        loginBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        joinBtn.setOnClickListener{
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        val prefs = getSharedPreferences("user", 0)
        val id : String = prefs.getString("id","").toString()

        db.collection("orders")
            .whereEqualTo("id", id)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.MODIFIED -> notify(dc.document.get("finish").toString().toBoolean())
                    }
                }
            }


    }

    fun notify(finish : Boolean) {
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

            var notificationBuilder = NotificationCompat.Builder(this, "음식완성")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("음식 완성")
                .setContentText("음식을 수령해주세요")
                .setAutoCancel(true)
                .setSound(notificationSound)
                .setContentIntent(pendingIntent)

            notificationManager.notify(0, notificationBuilder.build())

        }
    }

}