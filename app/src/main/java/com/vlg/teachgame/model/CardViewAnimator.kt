package com.vlg.teachgame.model

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd

class CardViewAnimator(
    private val cardView: CardView,
    private val screenWidth: Int
) {
    private var currentQuestionIndex = 0
    private var questions: List<String> = emptyList()
    private var onQuestionShow: ((String) -> Unit)? = null
    private var onAnswerProcessed: ((Boolean, Boolean) -> Unit)? = null
    private var onAnimationComplete: (() -> Unit)? = null
    private var isAnimating = false
    private var shouldShowNextQuestion = false

    fun setQuestions(questions: List<String>) {
        this.questions = questions
    }

    fun setOnQuestionShowListener(listener: (String) -> Unit) {
        this.onQuestionShow = listener
    }

    fun setOnAnswerProcessedListener(listener: (Boolean, Boolean) -> Unit) {
        this.onAnswerProcessed = listener
    }

    fun setOnAnimationCompleteListener(listener: () -> Unit) {
        this.onAnimationComplete = listener
    }

    fun startAnimationCycle() {
        if (questions.isEmpty()) return
        currentQuestionIndex = 0
        showNextQuestion()
    }

    fun showNextQuestion() {
        if (isAnimating || questions.isEmpty()) return

        val question = questions[currentQuestionIndex]
        onQuestionShow?.invoke(question)
        shouldShowNextQuestion = true

        performEnterAnimation()
    }

    private fun performEnterAnimation() {
        isAnimating = true

        cardView.translationX = -screenWidth.toFloat()
        cardView.alpha = 0f

        val enterAnimatorX = ObjectAnimator.ofFloat(
            cardView,
            "translationX",
            -screenWidth.toFloat(),
            0f
        )
        enterAnimatorX.duration = 1200
        enterAnimatorX.interpolator = OvershootInterpolator(0.5f)

        val fadeInAnimator = ObjectAnimator.ofFloat(cardView, "alpha", 0f, 1f)
        fadeInAnimator.duration = 600

        val set = AnimatorSet()
        set.playTogether(enterAnimatorX, fadeInAnimator)
        set.doOnEnd {
            isAnimating = false
            onAnimationComplete?.invoke()
        }
        set.start()
    }

    fun processAnswer(isAnswerAccepted: Boolean, isTeacherMistake: Boolean) {
        if (isAnimating) return
        isAnimating = true

        onAnswerProcessed?.invoke(isAnswerAccepted, isTeacherMistake)

        if (isAnswerAccepted) {
            val bounceAnimator = ObjectAnimator.ofFloat(cardView, "scaleY", 1f, 0.95f, 1f)
            bounceAnimator.duration = 200

            val exitAnimator = ObjectAnimator.ofFloat(cardView, "translationX", 0f, screenWidth.toFloat())
            exitAnimator.duration = 800
            exitAnimator.interpolator = AccelerateInterpolator()

            val fadeOutAnimator = ObjectAnimator.ofFloat(cardView, "alpha", 1f, 0f)
            fadeOutAnimator.duration = 400

            val exitSet = AnimatorSet()
            exitSet.playTogether(exitAnimator, fadeOutAnimator)

            val sequence = AnimatorSet()
            sequence.play(bounceAnimator).before(exitSet)
            sequence.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)

                    currentQuestionIndex = (currentQuestionIndex + 1) % questions.size
                    shouldShowNextQuestion = true

                    Handler(Looper.getMainLooper()).postDelayed({
                        val nextQuestion = questions[currentQuestionIndex]
                        onQuestionShow?.invoke(nextQuestion)
                        performEnterAnimation()
                    }, 500)
                }
            })
            sequence.start()
        } else {
            isAnimating = false
            shouldShowNextQuestion = false
        }
    }

    fun getCurrentIndex(): Int = currentQuestionIndex

    fun getCurrentQuestion(): String? {
        return if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
            questions[currentQuestionIndex]
        } else {
            null
        }
    }

    fun reset() {
        currentQuestionIndex = 0
        isAnimating = false
        cardView.clearAnimation()
        cardView.translationX = 0f
        cardView.alpha = 1f
        cardView.scaleY = 1f
        shouldShowNextQuestion = false
    }
}