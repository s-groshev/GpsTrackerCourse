package com.nosta.gpstrackercourse.location

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.nosta.gpstrackercourse.MainActivity
import com.nosta.gpstrackercourse.R

class LocationService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startNotification()
        isRunning = true
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
//        Log.d("MyLog", "LocationService::onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
//        Log.d("MyLog", "LocationService::onDestroy()")
        isRunning = false
    }

    private fun startNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ncChanel = NotificationChannel(
                CHANNEL_ID,
                "Location Service",
                NotificationManager.IMPORTANCE_HIGH
            )
            val nManager = getSystemService(NotificationManager::class.java) as NotificationManager
            nManager.createNotificationChannel(ncChanel)
            val req = NotificationManagerCompat.from(this).areNotificationsEnabled()
        }

        val nIntent = Intent(this, MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(
            this,
            10,
            nIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT } else { PendingIntent.FLAG_UPDATE_CURRENT }
        )
        val notification = NotificationCompat.Builder(
            this,
            CHANNEL_ID
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Tracker Running")
            .setContentIntent(pIntent)
            .build()
        startForeground(99, notification)
    }

    companion object {
        const val CHANNEL_ID = "channel_1"
        var isRunning = false
    }
}