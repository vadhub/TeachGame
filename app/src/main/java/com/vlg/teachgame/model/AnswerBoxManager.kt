package com.vlg.teachgame.model

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.vlg.teachgame.data.Question
import kotlin.random.Random

class AnswerBoxManager(
    private val cardViews: List<CardView>,
    private val answers: List<TextView>
) {

    private var current: TextView? = null
    private var currentCardView: CardView? = null

    fun setAnswers(question: Question, exWords: List<String>) {

        hideCurrentCard()

        val randomIndex = Random.nextInt(answers.size - 1)
        answers[randomIndex].text = exWords[0] + " " + question.correctAnswer

        answers.filterIndexed { index, _ -> index != randomIndex }.forEachIndexed { i, textView ->
            textView.text = exWords[(i+1) % exWords.size] + " " + question.incorrectAnswers[i]
        }
    }

    fun showCard(cardId: Int, animate: Boolean = true) {
        val card = cardViews[cardId]
        val text = answers[cardId]

        current = answers[cardId]
        currentCardView = cardViews[cardId]

        if (animate) {
            card.visibility = View.VISIBLE
            card.alpha = 0f
            card.scaleX = 0.8f
            card.scaleY = 0.8f

            card.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(100)
                .start()
        } else {
            card.visibility = View.VISIBLE
            card.alpha = 1f
        }

        text.visibility = View.VISIBLE
    }

    fun hideCard(cardId: Int, animate: Boolean = true) {
        val card = cardViews[cardId]
        val text = answers[cardId]

        current = answers[cardId]
        currentCardView = cardViews[cardId]

        if (animate) {
            card.animate()
                .alpha(0f)
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction {
                    card.visibility = View.INVISIBLE
                    text.visibility = View.INVISIBLE
                }
                .start()
        } else {
            card.visibility = View.INVISIBLE
            card.alpha = 0f
            text.visibility = View.INVISIBLE
        }
    }

    fun hideCurrentCard(animate: Boolean = true) {
        val card = currentCardView
        val text = current

        if (animate) {
            card?.animate()?.alpha(0f)?.scaleX(0.8f)?.scaleY(0.8f)?.setDuration(100)
                ?.withEndAction {
                    card.visibility = View.INVISIBLE
                    text?.visibility = View.INVISIBLE
                }?.start()

        } else {
            card?.visibility = View.INVISIBLE
            card?.alpha = 0f
            text?.visibility = View.INVISIBLE
        }
    }

}