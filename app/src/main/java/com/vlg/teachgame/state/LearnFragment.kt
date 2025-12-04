package com.vlg.teachgame.state

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.vlg.teachgame.AnswerButtonManager
import com.vlg.teachgame.CardViewAnimator
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R

class LearnFragment : Fragment() {

    private lateinit var navigator: Navigator

    private lateinit var table1: FrameLayout
    private lateinit var table2: FrameLayout
    private lateinit var table3: FrameLayout
    private lateinit var table4: FrameLayout

    private lateinit var hand1: ImageView
    private lateinit var hand2: ImageView
    private lateinit var hand3: ImageView
    private lateinit var hand4: ImageView

    private lateinit var cardViewAnimator: CardViewAnimator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.state_learn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        table1 = view.findViewById(R.id.table1)
        table2 = view.findViewById(R.id.table2)
        table3 = view.findViewById(R.id.table3)
        table4 = view.findViewById(R.id.table4)

        hand1 = view.findViewById(R.id.hand1)
        hand2 = view.findViewById(R.id.hand2)
        hand3 = view.findViewById(R.id.hand3)
        hand4 = view.findViewById(R.id.hand4)

        val trueButton = view.findViewById<Button>(R.id.trueButton)
        val falseButton = view.findViewById<Button>(R.id.falseButton)

        val cardView = view.findViewById<CardView>(R.id.query)
        val textQuery = view.findViewById<TextView>(R.id.textQuery)

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val answerButtonManager = AnswerButtonManager(trueButton, falseButton)
        answerButtonManager.enableButtons()
        answerButtonManager.visible()

        cardViewAnimator = CardViewAnimator(cardView, screenWidth)

        val questions = listOf(
            "Вопрос 1?",
            "Вопрос 2?",
            "Вопрос 3?"
        )

        cardViewAnimator.setQuestions(questions)

        cardViewAnimator.setOnQuestionShowListener { question ->
            textQuery.text = question
            enableAnswerButtons(true)
        }

        cardViewAnimator.setOnAnswerProcessedListener { isCorrect ->
            showAnswerResult(isCorrect)
        }

        cardViewAnimator.startAnimationCycle()

        trueButton.setOnClickListener {
            cardViewAnimator.processAnswer(true)
            cardViewAnimator.startAnimationCycle()

        }

        falseButton.setOnClickListener {
            cardViewAnimator.processAnswer(false)
            cardViewAnimator.startAnimationCycle()
        }

    }

    private fun enableAnswerButtons(enabled: Boolean) {
    }

    private fun showAnswerResult(isCorrect: Boolean) {
    }

    private fun showAnswerButtons() {
    }

    private fun hideAnswerButtons() {
    }


}