package com.example.codepathflashcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.ToggleButton
import androidx.activity.result.contract.ActivityResultContracts

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
        val addBtn = findViewById<ImageView>(R.id.addButton)
        val editBtn = findViewById<ImageView>(R.id.editButton)
        val toggle = findViewById<ToggleButton>(R.id.toggleAns)


        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) {
                val qString = data.getStringExtra("QUESTION_KEY")
                val aString = data.getStringExtra("ANSWER_KEY")
                val w1String  = data.getStringExtra("WRONG_1_ANSWER_KEY")
                val w2String = data.getStringExtra("WRONG_2_ANSWER_KEY")
                flashQst.text = qString
                flashAns.text = aString

                var arrayOfAnswers = arrayOf(aString, w1String, w2String)

                arrayOfAnswers.shuffle()

                flashA1.text = arrayOfAnswers[0]
                flashA2.text = arrayOfAnswers[1]
                flashA3.text = arrayOfAnswers[2]
            }
        }

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

        addBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        editBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("ORIGINAL_QUESTION_KEY", flashQst.text.toString());
            intent.putExtra("ORIGINAL_ANSWER_KEY", flashAns.text.toString());
            resultLauncher.launch(intent)
        }

    }
}