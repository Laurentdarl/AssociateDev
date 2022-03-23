package com.laurentdarl.associatedev.presenter.fragments.app.alarms

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.laurentdarl.associatedev.R
import com.laurentdarl.associatedev.databinding.FragmentAlarmManagerBinding
import com.laurentdarl.associatedev.domain.notifications.AlarmManagers
import com.laurentdarl.associatedev.domain.notifications.NOTIFICATION_CHANNEL
import com.laurentdarl.associatedev.presenter.fragments.app.home.MainFragmentDirections
import java.util.*

class AlarmManagerFragment : Fragment() {

    private var _binding: FragmentAlarmManagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var pendingIntent: PendingIntent
    private lateinit var picker: MaterialTimePicker
    private lateinit var alarmManager: AlarmManager
    private lateinit var calendar: Calendar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmManagerBinding.inflate(layoutInflater)

        createNotificationChannel()

        binding.btnSelectTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSetAlarm.setOnClickListener {
            setAlarm()
        }

        binding.btnCancelAlarm.setOnClickListener {
            cancelAlarm()
        }

        binding.btnReturn.setOnClickListener {
            findNavController().navigate(AlarmManagerFragmentDirections.actionAlarmManagerFragmentToMainFragment())
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun cancelAlarm() {
        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmManagers::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)

        alarmManager.cancel(pendingIntent)
        Toast.makeText(requireContext(), "Alarm cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun setAlarm() {
        alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(requireContext(), AlarmManagerFragment::class.java)
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY, pendingIntent
        )
    }

    private fun showTimePicker() {
        picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Select alarm time")
            .build()

        picker.show(requireActivity().supportFragmentManager, NOTIFICATION_CHANNEL)
        picker.addOnPositiveButtonClickListener {
            if (picker.hour > 12) {
                binding.tvAlarmTime.text =
                    String.format("%02d", picker.hour - 12) + " : " + String.format(
                        "%02d",
                        picker.minute
                    ) + "PM"

            } else {
                String.format("%02d", picker.hour) + " : " + String.format(
                    "%02d",
                    picker.minute
                ) + "AM"
            }
            calendar = Calendar.getInstance()
            calendar[Calendar.HOUR_OF_DAY] = picker.hour
            calendar[Calendar.MINUTE] = picker.minute
            calendar[Calendar.SECOND] = 0
            calendar[Calendar.MILLISECOND] = 0
            Toast.makeText(
                requireContext(),
                "Alarm set to" + calendar.time.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Alarm notification"
            val descr = "An alarm notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL, name, importance)
            channel.description = descr

            val notificationManager =
                requireActivity().getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

}