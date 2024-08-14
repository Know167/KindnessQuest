package com.example.mp_assignment3

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CompletedActsActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_acts)
        val tvCompletedAct=findViewById<TextView>(R.id.tvCompletedAct)
        val completedActs = getCompletedActs()
        if (completedActs.isNotEmpty()) {
            tvCompletedAct.text = completedActs.joinToString("\n")
        } else {
            tvCompletedAct.text = "No completed acts yet."
        }
    }

    private fun getCompletedActs(): List<String> {
        val sharedPreferences = getSharedPreferences("completed_tasks", MODE_PRIVATE)
        return sharedPreferences.getStringSet("completed_acts", emptySet())?.toList() ?: emptyList()
    }
}