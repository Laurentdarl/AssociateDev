package com.laurentdarl.associatedev.domain.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.laurentdarl.associatedev.R

const val NOTIFICATION_ID = 1
const val NOTIFICATION_CHANNEL = "channel1"
const val TITLE_EXTRA = "titleExtra"
const val MESSAGE_EXTRA = "messageExtra"

class LocalNotification: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_search)
            .setContentTitle(intent.getStringExtra(TITLE_EXTRA))
            .setContentText(intent.getStringExtra(MESSAGE_EXTRA))
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }
}