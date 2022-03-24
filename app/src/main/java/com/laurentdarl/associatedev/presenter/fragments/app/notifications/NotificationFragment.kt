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
import androidx.core.app.RemoteInput
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.databinding.FragmentNotificationBinding
import com.laurentdarl.associatedev.domain.notifications.*
import com.laurentdarl.associatedev.presenter.activity.MainActivity
import com.laurentdarl.associatedev.presenter.activity.RandomTestActivity
import com.laurentdarl.associatedev.presenter.fragments.app.alarms.AlarmManagerFragment
import com.laurentdarl.associatedev.presenter.fragments.app.favorite.FavoriteFragment
import com.laurentdarl.associatedev.presenter.fragments.app.home.MainFragmentArgs
import java.util.*


class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val KEY_REPLY =
        "Key"  // A reply notification requires a key used to receive users input

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(layoutInflater)

        createNotificationChannel()

        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
//        val fragmentIntents = findNavController().navigate(NotificationFragmentDirections.actionNotificationFragmentToMainFragment())
//        val pendingIntent = TaskStackBuilder.create(requireContext()).run {
//            addNextIntent(intent)
//            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//        }

        binding.btnSendNotification.setOnClickListener {
            createNotification(
                0,
                NOTIFICATION_CHANNEL,
                pendingIntent,
                "My Notification Title",
                "The real reason I am doing this is to display notifications in my app."
            )
//            sendNotification()
        }

        binding.btnReplyNotification.setOnClickListener {
            createActionNotification(
                0,
                "NOTIFICATION_CHANNEL"
            )
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

        val intent2 = Intent(requireContext(), AlarmManagerFragment::class.java)
        val pendingIntent2 = PendingIntent.getActivity(
            requireContext(),
            0,
            intent2,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
//        val bundle = Bundle()
//        bundle.putString("EXTRA_LETTER", args.toString())
//        val pendingIntent = NavDeepLinkBuilder(requireContext())
//            .setComponentName(MainActivity::class.java)
//            .setGraph(R.navigation.nav_graph)
//            .setDestination(R.id.alarmManagerFragment)
//            .setArguments(bundle)
//            .createPendingIntent()

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

    private fun createNotification(
        channelID: Int,
        channelName: String,
        pendingIntent: PendingIntent,
        title: String,
        content: String?
    ) {
        val notification = NotificationCompat.Builder(requireContext(), channelName)
            .setSmallIcon(R.drawable.ic_search)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(requireContext())) {
            notify(channelID, notification)
        }
    }

    private fun createActionNotification(
        channelID: Int,
        channelName: String
    ) {
        val intent = Intent(requireContext(), AlarmManagerFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val intent2 = Intent(requireContext(), RandomTestActivity::class.java)
        val pendingIntent2 = PendingIntent.getActivity(
            requireContext(),
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Add the ability to reply a notification from notification badge
        val remoteInput = RemoteInput.Builder(KEY_REPLY).run {
            setLabel("Insert Text Here")
            build()
        }
        val replyAction = NotificationCompat.Action.Builder(0, "Reply", pendingIntent2)
            .addRemoteInput(remoteInput).build()

        val action = NotificationCompat.Action.Builder(0, "Details", pendingIntent).build()
        val action2 = NotificationCompat.Action.Builder(0, "Cancel", pendingIntent2).build()

        val notification = NotificationCompat.Builder(requireContext(), channelName)
            .setSmallIcon(R.drawable.ic_search)
            .setContentTitle("A Notification with action buttons")
            .setContentText("This notification tests if the action directs you to a new screen when clicked.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(action)
            .addAction(action2)
            .addAction(replyAction)
            .setContentIntent(pendingIntent2)
            .build()

        with(NotificationManagerCompat.from(requireContext())) {
            notify(2, notification)
        }
    }

}