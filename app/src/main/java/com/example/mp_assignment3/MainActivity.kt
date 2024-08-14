package com.example.mp_assignment3

import android.Manifest
import androidx.activity.enableEdgeToEdge
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private val randomActs = listOf(
        "Compliment a stranger",
        "Draw something in 5 minutes",
        "Leave a positive note in a public place",
        "Donate to a charity",
        "Help someone with a task",
        "Call a friend you haven’t spoken to in a while",
        "Pick up litter in your neighborhood",
        "Plant a tree or a plant"
    )
    private lateinit var sharedPreferences: SharedPreferences
    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            showNotification()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("completed_tasks", Context.MODE_PRIVATE)

        val btnNewAct = findViewById<Button>(R.id.btnNewAct)
        val btnMarkComplete = findViewById<Button>(R.id.btnMarkComplete)
        val btnViewCompletedActs = findViewById<Button>(R.id.btnViewCompletedActs)
        val tvAct = findViewById<TextView>(R.id.tvAct)

        generateRandomAct(tvAct)

        btnNewAct.setOnClickListener {
            generateRandomAct(tvAct)
        }

        btnMarkComplete.setOnClickListener {
            val completedAct = tvAct.text.toString()
            if (completedAct.isNotEmpty()) {
                addCompletedAct(completedAct)
                val intent = Intent(this, CompletedActsActivity::class.java)
                startActivity(intent)
            }
        }

        btnViewCompletedActs.setOnClickListener {
            val intent = Intent(this, CompletedActsActivity::class.java)
            startActivity(intent)
        }

        createNotificationChannel()
        requestNotificationPermission()
    }

    private fun generateRandomAct(tvAct: TextView) {
        val randomIndex = Random.nextInt(randomActs.size)
        val randomAct = randomActs[randomIndex]
        tvAct.text = randomAct
    }

    private fun addCompletedAct(act: String) {
        val completedActs = getCompletedActs().toMutableList()
        completedActs.add(act)
        sharedPreferences.edit().putStringSet("completed_acts", completedActs.toSet()).apply()
    }

    private fun getCompletedActs(): Set<String> {
        return sharedPreferences.getStringSet("completed_acts", emptySet()) ?: emptySet()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Random Act Channel"
            val descriptionText = "Channel for Random Act notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("random_act_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                showNotification()
            }
        } else {
            showNotification()
        }
    }

    private fun showNotification() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, "random_act_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Random Act Generator")
            .setContentText("Try a new random act today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@with
            }
            notify(0, builder.build())
        }
    }
}