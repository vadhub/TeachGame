package com.vlg.teachgame.state

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.vlg.teachgame.model.AnswerButtonManager
import com.vlg.teachgame.model.CardViewAnimator
import com.vlg.teachgame.model.HintAnimationManager
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.AnswerBoxManager

class LearnFragment : Fragment() {

    private lateinit var navigator: Navigator

    private lateinit var table1: FrameLayout
    private lateinit var table2: FrameLayout
    private lateinit var table3: FrameLayout
    private lateinit var table4: FrameLayout

    private lateinit var cardAnswer1: CardView
    private lateinit var cardAnswer2: CardView
    private lateinit var cardAnswer3: CardView
    private lateinit var cardAnswer4: CardView

    private lateinit var textAnswer1: TextView
    private lateinit var textAnswer2: TextView
    private lateinit var textAnswer3: TextView
    private lateinit var textAnswer4: TextView

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

        cardAnswer1 = view.findViewById(R.id.queryTable1)
        cardAnswer2 = view.findViewById(R.id.queryTable2)
        cardAnswer3 = view.findViewById(R.id.queryTable3)
        cardAnswer4 = view.findViewById(R.id.queryTable4)

        textAnswer1 = view.findViewById(R.id.textQueryTable1)
        textAnswer2 = view.findViewById(R.id.textQueryTable2)
        textAnswer3 = view.findViewById(R.id.textQueryTable3)
        textAnswer4 = view.findViewById(R.id.textQueryTable4)

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

        val answerBoxManager = AnswerBoxManager(
            listOf(cardAnswer1, cardAnswer2, cardAnswer3, cardAnswer4),
            listOf(textAnswer1, textAnswer2, textAnswer3, textAnswer4)
        )

        val hintAnimationManager1 = HintAnimationManager(hand1)
        val hintAnimationManager2 = HintAnimationManager(hand2)
        val hintAnimationManager3 = HintAnimationManager(hand3)
        val hintAnimationManager4 = HintAnimationManager(hand4)

        hintAnimationManager1.startSmoothHintAnimation()
        hintAnimationManager2.startSmoothHintAnimation()
        hintAnimationManager3.startSmoothHintAnimation()
        hintAnimationManager4.startSmoothHintAnimation()

        cardViewAnimator = CardViewAnimator(cardView, screenWidth)

        val questions = listOf(
            Question(
                "Сколько будет 1 в 0 степени?",
                "Будет 1",
                listOf("Будет 0", "Думаю 64", " Может -1?")
            ),
            Question("Какая столица Германии?", "Берлин", listOf("Вена", "Юпитер", "Волгоград")),
            Question(
                "Сколько основных цетов?",
                "Три цвета",
                listOf("Наверное 7", "Думаю 4", " Может 7?")
            ),
            Question("Сколько будет 2 + 2 * 2?", "6", listOf("8", "10", "52!")),
        )

        answerBoxManager.setAnswers(questions[0])
        var answer = ""
        var trueAnswer = questions[0].answerTrue

        cardViewAnimator.setQuestions(questions.map { it.text })

        cardViewAnimator.setOnAnimationCompleteListener {
            val question = questions[cardViewAnimator.getCurrentIndex()]
            trueAnswer = question.answerTrue
            answerBoxManager.setAnswers(question)
            visibleHand()
        }

        cardViewAnimator.setOnQuestionShowListener { question ->
            textQuery.text = question
            enableAnswerButtons(true)
        }

        cardViewAnimator.setOnAnswerProcessedListener { isCorrect ->
            Log.d("1!!", ""+isCorrect)
            showAnswerResult(isCorrect)
        }

        cardViewAnimator.startAnimationCycle()

        trueButton.setOnClickListener {
            cardViewAnimator.processAnswer(trueAnswer == answer)
            answerButtonManager.gone()
            goneHand()
        }

        falseButton.setOnClickListener {
            cardViewAnimator.processAnswer(trueAnswer == answer)
            answerButtonManager.gone()
            goneHand()
        }

        table1.setOnClickListener {
            answerBoxManager.showCard(0)
            goneHand()
            answerButtonManager.enableButtons()
            answerButtonManager.visible()
            answer = textAnswer1.text.toString()
        }
        table2.setOnClickListener {
            answerBoxManager.showCard(1)
            goneHand()
            answerButtonManager.enableButtons()
            answerButtonManager.visible()
            answer = textAnswer2.text.toString()
        }
        table3.setOnClickListener {
            answerBoxManager.showCard(2)
            goneHand()
            answerButtonManager.enableButtons()
            answerButtonManager.visible()
            answer = textAnswer3.text.toString()
        }
        table4.setOnClickListener {
            answerBoxManager.showCard(3)
            goneHand()
            answerButtonManager.enableButtons()
            answerButtonManager.visible()
            answer = textAnswer4.text.toString()
        }

    }

    private fun visibleHand() {
        hand1.visibility = View.VISIBLE
        hand2.visibility = View.VISIBLE
        hand3.visibility = View.VISIBLE
        hand4.visibility = View.VISIBLE
    }

    private fun goneHand() {
        hand1.visibility = View.GONE
        hand2.visibility = View.GONE
        hand3.visibility = View.GONE
        hand4.visibility = View.GONE
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