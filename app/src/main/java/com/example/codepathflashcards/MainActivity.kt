package com.example.codepathflashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashQst = findViewById<TextView>(R.id.flashcard_question)
        val flashAns = findViewById<TextView>(R.id.flashcard_answer)
        flashQst.setOnClickListener {
            flashQst.visibility = View.INVISIBLE
            flashAns.visibility = View.VISIBLE
        }
        flashAns.setOnClickListener {
            flashAns.visibility = View.INVISIBLE
            flashQst.visibility = View.VISIBLE
        }
    }
}