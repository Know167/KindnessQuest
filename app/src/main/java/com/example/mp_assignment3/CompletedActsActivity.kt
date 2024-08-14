package com.example.mp_assignment3

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView

class CompletedActsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_acts)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCompletedActs)
        val emptyPlaceholder = findViewById<MaterialTextView>(R.id.tvEmptyListPlaceholder)

        val completedActs = getCompletedActs()

        if (completedActs.isNotEmpty()) {
            emptyPlaceholder.visibility = View.GONE
            recyclerView.adapter = CompletedActAdapter(completedActs)
        } else {
            emptyPlaceholder.visibility = View.VISIBLE
        }
    }

    private fun getCompletedActs(): List<String> {
        val sharedPreferences = getSharedPreferences("completed_tasks", MODE_PRIVATE)
        return sharedPreferences.getStringSet("completed_acts", emptySet())?.toList() ?: emptyList()
    }
}
