package com.laurentdarl.associatedev.presenter.activity

import android.app.NotificationManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.databinding.ActivityRandomTestBinding
import com.laurentdarl.associatedev.domain.notifications.NOTIFICATION_CHANNEL

class RandomTestActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityRandomTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receiveNotificationInput()
    }

    private fun receiveNotificationInput() {
        val KEY_REPLY =
            "Key"  // A reply notification requires a matching key used to receive users input
        val intent = this.intent
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        if (remoteInput != null) {
            val inputString = remoteInput.getCharSequence(KEY_REPLY).toString()
            binding.tvNotificationInput.text = inputString

            // To update the notification
            //1. use the same channelId you used to create the notification
            //2. use the same notificationId you used to create the notification

            val channelId = NOTIFICATION_CHANNEL
            val notificationID = 2

            val repliedNotification = NotificationCompat.Builder(this@RandomTestActivity, channelId)
                .setSmallIcon(R.drawable.ic_search)
                .setContentText("Received reply")
                .build()

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(notificationID, repliedNotification)
        }
    }
}