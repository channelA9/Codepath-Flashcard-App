package com.example.codepathflashcards

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val flashQst = findViewById<TextView>(R.id.flashcard_question)
        val flashAns = findViewById<TextView>(R.id.flashcard_answer)
        var flashSelection = findViewById<RelativeLayout>(R.id.answers)
        val flashA1 = findViewById<TextView>(R.id.answer_1)
        val flashA2 = findViewById<TextView>(R.id.answer_2)
        val flashA3 = findViewById<TextView>(R.id.answer_3)

        val toggle = findViewById<ToggleButton>(R.id.toggleAns)

        var showAnswers = true
        var answered = false

        // inner logic
        fun cleanAns() {
            flashA1.setBackgroundResource(R.drawable.front_card)
            flashA2.setBackgroundResource(R.drawable.front_card)
            flashA3.setBackgroundResource(R.drawable.front_card)
        }
        fun checkAns(a:TextView) {
            if(!answered) {
                answered = true
                flashA1.setBackgroundResource(R.drawable.back_card)
                flashA2.setBackgroundResource(R.drawable.back_card)
                flashA3.setBackgroundResource(R.drawable.back_card)
                if (a.text == flashAns.text) a.setBackgroundResource(R.drawable.right_card)
                else a.setBackgroundResource(R.drawable.wrong_card)
            }
            else {
                cleanAns()
                answered = false
            }
        }

        // flashcard

        flashQst.setOnClickListener {
            if(!showAnswers) {
                flashQst.visibility = View.INVISIBLE
                flashAns.visibility = View.VISIBLE
            }
        }
        flashAns.setOnClickListener {
            flashAns.visibility = View.INVISIBLE
            flashQst.visibility = View.VISIBLE
        }

        // answers

        flashA1.setOnClickListener {
            checkAns(flashA1)
        }
        flashA2.setOnClickListener {
            checkAns(flashA2)
        }
        flashA3.setOnClickListener {
            checkAns(flashA3)
        }

        // toggle

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                cleanAns()
                showAnswers = true
                answered = false
                flashSelection.visibility = View.VISIBLE

                flashQst.visibility = View.VISIBLE
                flashAns.visibility = View.INVISIBLE
            } else {
                showAnswers = false
                flashSelection.visibility = View.GONE
            }
        }

    }
}