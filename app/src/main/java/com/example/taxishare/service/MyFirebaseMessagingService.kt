/*
 * Created by WonJongSeong on 2019-08-04
 */

package com.example.taxishare.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.taxishare.R
import com.example.taxishare.app.Constant
import com.example.taxishare.view.login.LoginActivity
import com.example.taxishare.view.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        val TAG: String = MyFirebaseMessagingService::class.java.simpleName
    }


    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        // when not null
        remoteMessage?.from.apply {
            sendToActivity(
                applicationContext,
                remoteMessage?.from ?: "",
                remoteMessage?.notification?.title ?: "",
                remoteMessage?.notification?.body ?: "",
                remoteMessage?.data.toString()
            )
        }
    }

    override fun onSendError(p0: String?, p1: Exception?) {
        super.onSendError(p0, p1)
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
    }

    private fun sendToActivity(
        context: Context,
        from: String,
        title: String,
        body: String,
        contents: String
    ) {


        Log.d("FirebaseNotificationT", "$title $body")

        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context, "default")

        // 설정해둔 pending Intent를 이용해서 데이터를 여기다 보내주고
        // 시간, 언제, 어디서 출발하는지 정보 알려주기

        builder.setSmallIcon(R.drawable.ic_taxi_stop)
        builder.setContentTitle(title)
        builder.setContentText(body)

        // 액션 정의
        val intent2 = Intent(context, LoginActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent2,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        // 클릭 이벤트 설정
        builder.setContentIntent(pendingIntent)

        // 큰 아이콘 설정
        val largeIcon = BitmapFactory.decodeResource(context.resources, R.drawable.ic_taxi_stop)
        builder.setLargeIcon(largeIcon)

        // 색상 변경
        builder.color = Color.BLACK

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        // 오레오에서는 알림 채널을 매니저에 생성해야 한다
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager!!.createNotificationChannel(
                NotificationChannel(
                    "default",
                    "기본 채널",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }

        // 알림 통지
        manager!!.notify(1, builder.build())

        Intent(context, MainActivity::class.java).apply {
            //            putExtra("from", from)
//            putExtra("title", title)
//            putExtra("body", body)
//            putExtra("contents", contents)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(this)
        }
    }
}