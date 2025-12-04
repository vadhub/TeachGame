package com.vlg.teachgame.model

import android.animation.ObjectAnimator
import android.view.View
import android.widget.Button

class AnswerButtonManager(
    private val trueButton: Button,
    private val falseButton: Button
) {

    fun visible() {
        trueButton.visibility = View.VISIBLE
        falseButton.visibility = View.VISIBLE
    }

    fun gone() {
        trueButton.visibility = View.GONE
        falseButton.visibility = View.GONE
    }

    fun enableButtons() {
        trueButton.isEnabled = true
        falseButton.isEnabled = true
        trueButton.alpha = 1f
        falseButton.alpha = 1f
    }

    fun disableButtons() {
        trueButton.isEnabled = false
        falseButton.isEnabled = false
        trueButton.alpha = 0.5f
        falseButton.alpha = 0.5f
    }

    fun setOnAnswerClickListener(listener: (Boolean) -> Unit) {
        trueButton.setOnClickListener {
            listener(true)
            disableButtons()
        }

        falseButton.setOnClickListener {
            listener(false)
            disableButtons()
        }
    }

    fun animateButtonFeedback(isCorrect: Boolean) {
        val button = if (isCorrect) trueButton else falseButton

        ObjectAnimator.ofFloat(button, "scaleX", 1f, 1.2f, 1f).apply {
            setDuration(300)
            start()
        }

        ObjectAnimator.ofFloat(button, "scaleY", 1f, 1.2f, 1f).apply {
            setDuration(300)
            start()
        }
    }
}