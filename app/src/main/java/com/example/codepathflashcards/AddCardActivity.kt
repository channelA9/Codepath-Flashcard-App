package com.example.codepathflashcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val saveButton = findViewById<ImageView>(R.id.saveButton)
        val questionText = findViewById<EditText>(R.id.questionText)
        val answerText = findViewById<EditText>(R.id.answerText)
        val w1AnswerText = findViewById<EditText>(R.id.wrong1Text)
        val w2AnswerText = findViewById<EditText>(R.id.wrong2Text)

        // importing from intent

        val originalA =  intent.getStringExtra("ORIGINAL_ANSWER_KEY")
        val originalQ = intent.getStringExtra("ORIGINAL_QUESTION_KEY")


        questionText.setText(originalQ)
        answerText.setText(originalA)

        backButton.setOnClickListener {
            finish()
        }

        saveButton.setOnClickListener {

            val data = Intent() // create a new Intent, this is where we will put our data
            val qString = questionText.text.toString()
            val aString = answerText.text.toString()
            val w1String = w1AnswerText.text
            val w2String = w2AnswerText.text

            if (qString.isNotEmpty() && aString.isNotEmpty()) {
                data.putExtra(
                    "QUESTION_KEY",
                    qString
                )

                data.putExtra(
                    "ANSWER_KEY",
                    aString
                )

                if (qString.isNotEmpty() && aString.isNotEmpty()) {
                    data.putExtra(
                        "WRONG_1_ANSWER_KEY",
                        w1String
                    )

                    data.putExtra(
                        "WRONG_2_ANSWER_KEY",
                        w2String
                    )

                }

                setResult(RESULT_OK, data)

                Toast.makeText(applicationContext, "Successfully changed card!", Toast.LENGTH_SHORT).show();

                finish()
            }
            else {
                Toast.makeText(applicationContext, "Please fill in all the boxes!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}