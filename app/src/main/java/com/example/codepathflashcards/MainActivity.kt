package com.example.codepathflashcards

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        flashcardDatabase = FlashcardDatabase(this)

        allFlashcards = flashcardDatabase.getAllCards() as MutableList<Flashcard>

        val noCardView = findViewById<TextView>(R.id.noCardView)
        val flashCard = findViewById<FrameLayout>(R.id.flashcard)
        val leftRightBar = findViewById<RelativeLayout>(R.id.leftRight)

        val flashQst = findViewById<TextView>(R.id.flashcard_question)
        val flashAns = findViewById<TextView>(R.id.flashcard_answer)
        val flashSelection = findViewById<RelativeLayout>(R.id.answers)
        val flashA1 = findViewById<TextView>(R.id.answer_1)
        val flashA2 = findViewById<TextView>(R.id.answer_2)
        val flashA3 = findViewById<TextView>(R.id.answer_3)

        val delBtn = findViewById<ImageView>(R.id.deleteCard)
        val addBtn = findViewById<ImageView>(R.id.addButton)
        val editBtn = findViewById<ImageView>(R.id.editButton)
        val toggle = findViewById<ToggleButton>(R.id.toggleAns)
        val prevBtn = findViewById<ImageView>(R.id.previousCard)
        val nextBtn = findViewById<ImageView>(R.id.nextCard)
        val shuffleBtn = findViewById<ImageView>(R.id.shuffleCard)

        var showAnswers = true
        var tempShow = false
        var answered = false
        var indice = 0



        fun cleanAns() {
            answered = false
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
            }
        }

        fun shuffle3Ans(c:String,w1:String,w2:String) {
            val arrayOfAnswers = arrayOf(c, w1, w2)

            arrayOfAnswers.shuffle()

            flashA1.text = arrayOfAnswers[0]
            flashA2.text = arrayOfAnswers[1]
            flashA3.text = arrayOfAnswers[2]
        }

        fun updateCard(q:String,c:String,w1:String?,w2:String?) {
            flashAns.visibility = View.INVISIBLE
            flashQst.visibility = View.VISIBLE
            flashQst.text = q
            flashAns.text = c
            if (w1 != null && w2 != null) {
                tempShow = false
                if (showAnswers) { flashSelection.visibility = View.VISIBLE }
                toggle.visibility = View.VISIBLE
                shuffle3Ans(c, w1, w2)
            }
            else {
                tempShow = true
                toggle.visibility = View.INVISIBLE
                flashSelection.visibility = View.INVISIBLE
            }
        }

        fun readFromFlashCard(card:Flashcard?) {
            if (card != null) {
                updateCard(
                    card.question,
                    card.answer,
                    card.wrongAnswer1,
                    card.wrongAnswer2
                )
            }
        }

        fun checkIfCards() {
            if (allFlashcards.size > 0) {

                indice = 0
                updateCard(
                    allFlashcards[0].question,
                    allFlashcards[0].answer,
                    allFlashcards[0].wrongAnswer1,
                    allFlashcards[0].wrongAnswer2)

                noCardView.visibility = View.INVISIBLE

                flashCard.visibility = View.VISIBLE
                flashSelection.visibility = View.VISIBLE
                leftRightBar.visibility = View.VISIBLE
                editBtn.visibility = View.VISIBLE
                toggle.visibility = View.VISIBLE
            }
            else {
                noCardView.visibility = View.VISIBLE

                flashCard.visibility = View.INVISIBLE
                flashSelection.visibility = View.INVISIBLE
                leftRightBar.visibility = View.INVISIBLE
                editBtn.visibility = View.INVISIBLE
                toggle.visibility = View.INVISIBLE
            }
        }

        fun removeCard() {
            val flashcardQuestionToDelete = findViewById<TextView>(R.id.flashcard_question).text.toString()
            flashcardDatabase.deleteCard(flashcardQuestionToDelete)

            allFlashcards = flashcardDatabase.getAllCards().toMutableList()
            checkIfCards()
        }

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data: Intent? = result.data
            if (data != null) {

                val qString = data.getStringExtra("QUESTION_KEY")
                val aString = data.getStringExtra("ANSWER_KEY")
                val w1String  = data.getStringExtra("WRONG_1_ANSWER_KEY")
                val w2String = data.getStringExtra("WRONG_2_ANSWER_KEY")

                //db
                if (qString != null && aString != null) {
                    if (w1String != null && w2String != null) {
                        flashcardDatabase.insertCard(Flashcard(qString.toString(), aString.toString(),w1String.toString(),w2String.toString()))
                        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                        updateCard(qString,aString,w1String,w2String)
                    }
                    else {
                        flashcardDatabase.insertCard(Flashcard(qString.toString(), aString.toString()))
                        allFlashcards = flashcardDatabase.getAllCards().toMutableList()
                        updateCard(qString,aString,null,null)
                    }
                    checkIfCards()
                }

            }
        }



        //initialization to load existing Q's if existing:
        checkIfCards()

        // inner logic

        delBtn.setOnClickListener {
            removeCard()
        }
        // flashcard

        flashQst.setOnClickListener {
            if(!showAnswers or tempShow) {
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

        // look through existing db

        prevBtn.setOnClickListener {
            if (allFlashcards.size > 1){
                if (indice == 0) {
                    indice = allFlashcards.size - 1
                } else if (indice > 0) {
                    indice -= 1
                }
                readFromFlashCard(allFlashcards[indice])
            }
            else {
                Toast.makeText(applicationContext, "No other cards to show!", Toast.LENGTH_SHORT).show();
            }
        }

        nextBtn.setOnClickListener {
            cleanAns()
            if (allFlashcards.size > 1) {
                if (indice == allFlashcards.size - 1) {
                    indice = 0
                } else if (indice < allFlashcards.size - 1) {
                    indice += 1
                }
                readFromFlashCard(allFlashcards[indice])
            }
            else {
            Toast.makeText(applicationContext, "No other cards to show!", Toast.LENGTH_SHORT).show();
            }
        }

        shuffleBtn.setOnClickListener {
            if (allFlashcards.size > 1) {
                cleanAns()
                val oldI = indice
                while (indice == oldI) {
                    indice = (0 until allFlashcards.size - 1).random()
                }
                readFromFlashCard(allFlashcards[indice])
            }
            else {
                Toast.makeText(applicationContext, "No other cards to show!", Toast.LENGTH_SHORT).show();
            }
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
                flashSelection.visibility = View.INVISIBLE
            }
        }

        addBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            resultLauncher.launch(intent)
        }

        editBtn.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            intent.putExtra("ORIGINAL_QUESTION_KEY", flashQst.text.toString())
            intent.putExtra("ORIGINAL_ANSWER_KEY", flashQst.text.toString())
            resultLauncher.launch(intent)
        }

    }
}