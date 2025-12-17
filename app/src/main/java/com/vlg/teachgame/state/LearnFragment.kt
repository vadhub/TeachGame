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
import com.vlg.teachgame.model.AnswerButtonManager
import com.vlg.teachgame.model.CardViewAnimator
import com.vlg.teachgame.model.HintAnimationManager
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.GameManager
import com.vlg.teachgame.R
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.AnswerBoxManager

class LearnFragment : Fragment() {

    private lateinit var navigator: Navigator
    private lateinit var gameManager: GameManager

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
    private lateinit var answerBoxManager: AnswerBoxManager
    private lateinit var answerButtonManager: AnswerButtonManager

    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var selectedAnswer: String = ""
    private var isAnswerSelected = false
    private var isWaitingForEvaluation = false
    private var answeredTables = mutableSetOf<Int>()
    private var currentAnswerCorrect: Boolean = false
    private var exWords = mutableListOf<String>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
        gameManager = context as GameManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.state_learn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews(view)
        setupManagers(view)
        setupQuestions()
        setupClickListeners()
        startLearningCycle()
    }

    private fun initViews(view: View) {
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
    }

    private fun setupManagers(view: View) {
        val trueButton = view.findViewById<Button>(R.id.trueButton)
        val falseButton = view.findViewById<Button>(R.id.falseButton)
        val cardView = view.findViewById<CardView>(R.id.query)
        val textQuery = view.findViewById<TextView>(R.id.textQuery)

        val displayMetrics = resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        answerButtonManager = AnswerButtonManager(trueButton, falseButton)
        answerBoxManager = AnswerBoxManager(
            listOf(cardAnswer1, cardAnswer2, cardAnswer3, cardAnswer4),
            listOf(textAnswer1, textAnswer2, textAnswer3, textAnswer4)
        )

        cardViewAnimator = CardViewAnimator(cardView, screenWidth)

        setupHandAnimations()
        setupCardViewAnimatorListeners(textQuery)

        trueButton.setOnClickListener {
            if (!isAnswerSelected || isWaitingForEvaluation) return@setOnClickListener
            evaluateAnswer(true)
        }

        falseButton.setOnClickListener {
            if (!isAnswerSelected || isWaitingForEvaluation) return@setOnClickListener
            evaluateAnswer(false)
        }
    }

    private fun setupHandAnimations() {
        val hintAnimationManager1 = HintAnimationManager(hand1)
        val hintAnimationManager2 = HintAnimationManager(hand2)
        val hintAnimationManager3 = HintAnimationManager(hand3)
        val hintAnimationManager4 = HintAnimationManager(hand4)

        hintAnimationManager1.startSmoothHintAnimation()
        hintAnimationManager2.startSmoothHintAnimation()
        hintAnimationManager3.startSmoothHintAnimation()
        hintAnimationManager4.startSmoothHintAnimation()
    }

    private fun setupCardViewAnimatorListeners(textQuery: TextView) {
        cardViewAnimator.setOnQuestionShowListener { question ->
            textQuery.text = question
            resetAnswerState()
        }

        cardViewAnimator.setOnAnswerProcessedListener { isAnswerAccepted, isTeacherMistake ->

            if (isAnswerAccepted) {
                answerBoxManager.hideCurrentCard()
                answerButtonManager.gone()
                goneHand()
                currentQuestionIndex = cardViewAnimator.getCurrentIndex()+1
                answerBoxManager.setAnswers(questions[currentQuestionIndex], exWords)
                gameManager.increaseNumQuestion()
            } else {
                answerBoxManager.hideCurrentCard()
                answerButtonManager.gone()
                visibleHandForNextStudent()
            }
        }

        cardViewAnimator.setOnAnimationCompleteListener {
            visibleHandForAllStudents()
            isWaitingForEvaluation = false
        }
    }

    private fun setupQuestions() {
        exWords = mutableListOf("Наверное...", "Думаю", "Не знаю, наверное", "Может").shuffled() as MutableList<String>
        questions = gameManager.getQuestions().shuffled()
        questions = questions.subList(0, 5)
        cardViewAnimator.setQuestions(questions.map { it.text })
    }

    private fun setupClickListeners() {
        table1.setOnClickListener { onTableClicked(0) }
        table2.setOnClickListener { onTableClicked(1) }
        table3.setOnClickListener { onTableClicked(2) }
        table4.setOnClickListener { onTableClicked(3) }
    }

    private fun onTableClicked(tableIndex: Int) {
        if (isWaitingForEvaluation) return

        answeredTables.add(tableIndex)

        answerBoxManager.showCard(tableIndex)
        selectedAnswer = when (tableIndex) {
            0 -> textAnswer1.text.toString()
            1 -> textAnswer2.text.toString()
            2 -> textAnswer3.text.toString()
            3 -> textAnswer4.text.toString()
            else -> ""
        }

        currentAnswerCorrect = selectedAnswer == questions[currentQuestionIndex].correctAnswer

        isAnswerSelected = true
        goneHand()
        answerButtonManager.enableButtons()
        answerButtonManager.visible()
    }

    private fun evaluateAnswer(userEvaluation: Boolean) {
        if (!isAnswerSelected) return

        isWaitingForEvaluation = true
        answerButtonManager.disableButtons()

        val isAnswerAccepted = (currentAnswerCorrect && userEvaluation) ||
                (!currentAnswerCorrect && userEvaluation)

        val teacherMistake = !currentAnswerCorrect && userEvaluation

        cardViewAnimator.processAnswer(isAnswerAccepted, teacherMistake)
    }

    private fun startLearningCycle() {
        answeredTables.clear()
        val firstQuestion = questions[currentQuestionIndex]
        gameManager.increaseNumQuestion()
        answerBoxManager.setAnswers(firstQuestion, exWords)

        cardViewAnimator.showNextQuestion()
    }

    private fun resetAnswerState() {
        selectedAnswer = ""
        isAnswerSelected = false
        isWaitingForEvaluation = false
        answerButtonManager.disableButtons()
        answerButtonManager.gone()
    }

    private fun visibleHandForAllStudents() {
        hand1.visibility = View.VISIBLE
        hand2.visibility = View.VISIBLE
        hand3.visibility = View.VISIBLE
        hand4.visibility = View.VISIBLE

        answeredTables.clear()
    }

    private fun visibleHandForNextStudent() {
        if (answeredTables.size >= 4) {
            answeredTables.clear()
            visibleHandForAllStudents()
            resetAnswerState()
            return
        }

        hand1.visibility = if (0 in answeredTables) View.GONE else View.VISIBLE
        hand2.visibility = if (1 in answeredTables) View.GONE else View.VISIBLE
        hand3.visibility = if (2 in answeredTables) View.GONE else View.VISIBLE
        hand4.visibility = if (3 in answeredTables) View.GONE else View.VISIBLE

        resetAnswerState()
    }

    private fun goneHand() {
        hand1.visibility = View.GONE
        hand2.visibility = View.GONE
        hand3.visibility = View.GONE
        hand4.visibility = View.GONE
    }
}