package com.vlg.teachgame.state

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.vlg.teachgame.GameManager
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.Alive
import com.vlg.teachgame.model.CardViewAnimatorVertical
import com.vlg.teachgame.model.Management

class HomeworkFragment : Fragment() {

    private lateinit var navigator: Navigator
    private lateinit var gameManager: GameManager

    private var countStudents = 3
    private lateinit var cardAnimator: CardViewAnimatorVertical
    private lateinit var studentViews: List<ImageView>
    private var studentAnimators: MutableList<android.animation.Animator?> = mutableListOf()
    private var currentStudentIndex = -1

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
        return inflater.inflate(R.layout.state_check, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val displayMetrics = resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels

        val cardView = view.findViewById<CardView>(R.id.homework)

        // before start card with question is GONE visibility
        cardView.visibility = View.GONE

        val textHomeWork = view.findViewById<TextView>(R.id.textHomework)

        cardAnimator = CardViewAnimatorVertical(cardView, screenHeight)

        val verticalBoard = view.findViewById<View>(R.id.place)
        val nextButton = view.findViewById<Button>(R.id.next)
        val correctButton = view.findViewById<Button>(R.id.trueButton)
        val incorrectButton = view.findViewById<Button>(R.id.falseButton)

        val student3 = view.findViewById<ImageView>(R.id.vaysa)
        val student2 = view.findViewById<ImageView>(R.id.masha)
        val student1 = view.findViewById<ImageView>(R.id.petya)
        studentViews = listOf(student1, student2, student3)

        val allTextures = listOf(
            R.drawable.pers_1,
            R.drawable.pers_2,
            R.drawable.pers_3,
            R.drawable.pers_4
        )
        val selectedTextures = allTextures.shuffled().take(3)

        studentViews.forEachIndexed { index, imageView ->
            imageView.setImageResource(selectedTextures[index])
        }

        studentAnimators.clear()
        studentViews.forEach { student ->
            val animator = Alive().startWobbleAnimation(student)
            studentAnimators.add(animator)
        }

        var homework = gameManager.getHomeworks().shuffled()
        homework = homework.subList(0, 3)
        cardAnimator.setQuestions(homework)

        cardAnimator.setOnQuestionShowListener { question ->
            Log.d("!!!", question.toString())
            textHomeWork.text = question.text
        }

        cardAnimator.setOnAnswerProcessedListener { react, answerAccuracy ->
            Log.d("!!!", "react: " + react)
            gameManager.checkTeacher(answerAccuracy, react)
        }

        nextButton.setOnClickListener {
            goneNextButton(nextButton)

            // after click next cardview is visible
            if (countStudents > 0) {
                cardView.visibility = View.VISIBLE
                cardAnimator.showNextQuestion()
            }

            currentStudentIndex = countStudents - 1
            Log.d("!!!", currentStudentIndex.toString())
            if (currentStudentIndex in studentViews.indices) {
                val student = studentViews[currentStudentIndex]
                studentAnimators[currentStudentIndex]?.cancel()
                Alive().moveWithSpringAnimation(student, verticalBoard) {
                    visibleButtonEvaluate(correctButton, incorrectButton)
                    goneNextButton(nextButton)
                }
            }

            if (countStudents == 0) {
                gameManager.completeHomeWork()
            }

            countStudents--
        }

        correctButton.setOnClickListener {
            cardAnimator.processAnswer(true)
            if (currentStudentIndex != -1) {
                moveRight(studentViews[currentStudentIndex])
            }
            goneButtonEvaluate(correctButton, incorrectButton)
            visibleNextButton(nextButton)
        }

        incorrectButton.setOnClickListener {
            cardAnimator.processAnswer(false)
            if (currentStudentIndex != -1) {
                moveRight(studentViews[currentStudentIndex])
            }
            goneButtonEvaluate(correctButton, incorrectButton)
            visibleNextButton(nextButton)
        }

    }

    private fun moveRight(student: ImageView) {
        Alive().moveRightAndHide(student)
    }

    private fun visibleButtonEvaluate(correct: Button, incorrect: Button) {
        correct.visibility = View.VISIBLE
        incorrect.visibility = View.VISIBLE
    }

    private fun goneButtonEvaluate(correct: Button, incorrect: Button) {
        correct.visibility = View.GONE
        incorrect.visibility = View.GONE
    }

    private fun visibleNextButton(next: Button) {
        next.visibility = View.VISIBLE
    }

    private fun goneNextButton(next: Button) {
        next.visibility = View.GONE
    }
}