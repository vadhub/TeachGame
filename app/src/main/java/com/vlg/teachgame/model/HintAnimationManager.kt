package com.vlg.teachgame.model

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import kotlin.math.sin

class HintAnimationManager(private val targetView: View) {

    private var isAnimating = false
    private var isVisible = true
    private var currentAnimator: Animator? = null

    companion object {
        private const val MOVE_DISTANCE = 20f
        private const val ANIMATION_DURATION = 800L
        private const val FADE_DURATION = 300L
    }

    fun startHintAnimation() {
        if (isAnimating) return

        isAnimating = true

        val moveDownAnimator = ObjectAnimator.ofFloat(
            targetView,
            "translationY",
            0f,
            MOVE_DISTANCE
        ).apply {
            setDuration(ANIMATION_DURATION)
            interpolator = AccelerateDecelerateInterpolator()
        }

        val moveUpAnimator = ObjectAnimator.ofFloat(
            targetView,
            "translationY",
            MOVE_DISTANCE,
            0f
        ).apply {
            setDuration(ANIMATION_DURATION)
            interpolator = AccelerateDecelerateInterpolator()
        }

        val animatorSet = AnimatorSet().apply {
            play(moveDownAnimator).before(moveUpAnimator)
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    if (isAnimating) {
                        startHintAnimation()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {
                    isAnimating = false
                }

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        currentAnimator = animatorSet
        animatorSet.start()
    }

    fun startSmoothHintAnimation() {
        if (isAnimating) return

        isAnimating = true

        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            setDuration(ANIMATION_DURATION * 2)
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE

            addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                val translationY = sin(value * Math.PI).toFloat() * MOVE_DISTANCE
                targetView.translationY = translationY
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    isAnimating = false
                }

                override fun onAnimationCancel(animation: Animator) {
                    isAnimating = false
                }

                override fun onAnimationRepeat(animation: Animator) {}
            })
        }

        currentAnimator = animator
        animator.start()
    }

    fun stopHintAnimation() {
        isAnimating = false
        currentAnimator?.cancel()
        currentAnimator = null

        targetView.animate()
            .translationY(0f)
            .setDuration(200)
            .start()
    }

    fun show(animate: Boolean = true) {
        if (isVisible) return

        isVisible = true

        if (animate) {
            targetView.alpha = 0f
            targetView.visibility = View.VISIBLE

            targetView.animate()
                .alpha(1f)
                .setDuration(FADE_DURATION)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withStartAction {
                    targetView.visibility = View.VISIBLE
                }
                .start()
        } else {
            targetView.alpha = 1f
            targetView.visibility = View.VISIBLE
        }
    }

    fun hide(animate: Boolean = true) {
        if (!isVisible) return

        isVisible = false

        if (animate) {
            targetView.animate()
                .alpha(0f)
                .setDuration(FADE_DURATION)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .withEndAction {
                    targetView.visibility = View.INVISIBLE
                }
                .start()
        } else {
            targetView.alpha = 0f
            targetView.visibility = View.INVISIBLE
        }

        stopHintAnimation()
    }

    fun toggleVisibility(animate: Boolean = true) {
        if (isVisible) {
            hide(animate)
        } else {
            show(animate)
        }
    }

    fun playPressAnimation() {
        val scaleDownAnimator = ObjectAnimator.ofFloat(
            targetView,
            "scaleX",
            1f,
            0.9f
        ).apply {
            setDuration(100)
        }

        val scaleDownAnimatorY = ObjectAnimator.ofFloat(
            targetView,
            "scaleY",
            1f,
            0.9f
        ).apply {
            setDuration(100)
        }

        val scaleUpAnimator = ObjectAnimator.ofFloat(
            targetView,
            "scaleX",
            0.9f,
            1f
        ).apply {
            setDuration(100)
            interpolator = OvershootInterpolator()
        }

        val scaleUpAnimatorY = ObjectAnimator.ofFloat(
            targetView,
            "scaleY",
            0.9f,
            1f
        ).apply {
            setDuration(100)
            interpolator = OvershootInterpolator()
        }

        val animatorSet = AnimatorSet().apply {
            playTogether(scaleDownAnimator, scaleDownAnimatorY)
            play(scaleUpAnimator).after(scaleDownAnimator)
            play(scaleUpAnimatorY).after(scaleDownAnimatorY)
        }

        animatorSet.start()
    }

    fun setMoveDistance(distance: Float) {
        stopHintAnimation()
    }

    fun setAnimationSpeed(speed: Long) {
        stopHintAnimation()
    }

    fun isAnimating(): Boolean = isAnimating

    fun isVisible(): Boolean = isVisible

    fun cleanup() {
        stopHintAnimation()
        targetView.clearAnimation()
    }
}