package com.laurentdarl.associatedev.presenter.fragments.app.notifications

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.databinding.FragmentNotificationBinding
import com.laurentdarl.associatedev.domain.notifications.*
import com.laurentdarl.associatedev.presenter.fragments.app.alarms.AlarmManagerFragment
import java.util.*


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(layoutInflater)

        createNotificationChannel()

        val intent = Intent(requireContext(), AlarmManagerFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
//        val pendingIntent = TaskStackBuilder.create(requireContext()).run {
//            addNextIntent(intent)
//            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        }

        val notification = NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_search)
            .setContentTitle("My Notification Title")
            .setContentText("The real reason I am doing this is to display notifications in my app.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        binding.btnSendNotification.setOnClickListener {
//            sendNotification()
            with(NotificationManagerCompat.from(requireContext())) {
                notify(0, notification)
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun sendNotification() {
        val intent = Intent(requireContext(), LocalNotification::class.java)
        val title = binding.etTitle.text.toString()
        val message = binding.etMessage.text.toString()
        intent.putExtra(TITLE_EXTRA, title)
        intent.putExtra(MESSAGE_EXTRA, message)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        showAlert(time, title, message)
    }

    private fun showAlert(time: Long, title: String, message: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())
        val timeFormat = android.text.format.DateFormat.getTimeFormat((requireContext()))

        AlertDialog.Builder(requireContext())
            .setTitle("Notification scheduled")
            .setMessage(
                "Title: " + title +
                        "\nMessage: " + message +
                        "\nAt: " + dateFormat.format(date) + "" + timeFormat.format(date)
            )
            .setPositiveButton("Okay") { _, _ -> }
            .show()
    }

    private fun getTime(): Long {
        val minute = binding.timePicker.minute
        val hour = binding.timePicker.hour
        val day = binding.datePicker.dayOfMonth
        val month = binding.datePicker.month
        val year = binding.datePicker.year
        val calendar = Calendar.getInstance()

        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notification channel"
            val descr = "A description of the channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance).apply {
                description = descr
                lightColor = Color.RED
                enableLights(true)
                enableVibration(true)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PRIVATE
            }
            val notificationManager =
                requireActivity().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}