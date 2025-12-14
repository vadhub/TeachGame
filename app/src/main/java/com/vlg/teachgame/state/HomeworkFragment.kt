package com.vlg.teachgame.state

import android.content.Context
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R
import com.vlg.teachgame.model.Alive
import com.vlg.teachgame.model.CardViewAnimatorVertical

class HomeworkFragment : Fragment() {

    private lateinit var navigator: Navigator
    private var countStudents = 3
    private lateinit var cardAnimator: CardViewAnimatorVertical

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
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
        val textHomeWork = view.findViewById<TextView>(R.id.textHomework)

        cardAnimator = CardViewAnimatorVertical(cardView, screenHeight)

        val verticalBoard = view.findViewById<View>(R.id.place)
        val nextButton = view.findViewById<Button>(R.id.next)
        val correctButton = view.findViewById<Button>(R.id.trueButton)
        val incorrectButton = view.findViewById<Button>(R.id.falseButton)

        val petya = view.findViewById<ImageView>(R.id.petya)
        val vasya = view.findViewById<ImageView>(R.id.vaysa)
        val masha = view.findViewById<ImageView>(R.id.masha)

        val animatorSet1 = Alive().startWobbleAnimation(petya)
        val animatorSet2 = Alive().startWobbleAnimation(vasya)
        val animatorSet3 = Alive().startWobbleAnimation(masha)

        val questions = listOf(
            "Первый вопрос",
            "Второй вопрос",
            "Третий вопрос"
        )
        cardAnimator.setQuestions(questions)

        cardAnimator.setOnQuestionShowListener { question ->
            textHomeWork.text = question
        }

        cardAnimator.setOnAnswerProcessedListener { isAnswerAccepted, isTeacherMistake ->
            if (isAnswerAccepted) {

            } else {

            }
        }

        nextButton.setOnClickListener {

            cardAnimator.startAnimationCycle()
            when (countStudents) {
                3 -> {
                    animatorSet2.cancel()
                    Alive().moveWithSpringAnimation(vasya, verticalBoard) {
                        visibleButtonEvaluate(correctButton, incorrectButton)
                        goneNextButton(nextButton)
                    }
                }

                2 -> {
                    animatorSet3.cancel()
                    Alive().moveWithSpringAnimation(masha, verticalBoard) {
                        visibleButtonEvaluate(correctButton, incorrectButton)
                        goneNextButton(nextButton)
                    }
                }

                1 -> {
                    animatorSet1.cancel()
                    Alive().moveWithSpringAnimation(petya, verticalBoard) {
                        visibleButtonEvaluate(correctButton, incorrectButton)
                        goneNextButton(nextButton)
                    }
                }
            }

            countStudents--
        }

        correctButton.setOnClickListener {
            cardAnimator.processAnswer(true, false)
            moveRight(vasya, masha, petya)
            goneButtonEvaluate(correctButton, incorrectButton)
            visibleNextButton(nextButton)
        }

        incorrectButton.setOnClickListener {
            cardAnimator.processAnswer(false, false)
            moveRight(vasya, masha, petya)
            goneButtonEvaluate(correctButton, incorrectButton)
            visibleNextButton(nextButton)
        }

    }

    private fun moveRight(
        vasya: ImageView,
        masha: ImageView,
        petya: ImageView
    ) {
        when (countStudents) {
            2 -> {
                Alive().moveRightAndHide(vasya)
            }

            1 -> {
                Alive().moveRightAndHide(masha)
            }

            0 -> {
                Alive().moveRightAndHide(petya)
            }
        }
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