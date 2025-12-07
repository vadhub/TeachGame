package com.vlg.teachgame.model

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import kotlin.random.Random

class Alive {

    fun startWobbleAnimation(imageView: View): AnimatorSet {
        val animatorSet = AnimatorSet()

        val scaleY = ObjectAnimator.ofFloat(imageView, "scaleY", 1f, 0.93f, 1f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }
        scaleY.duration = Random.nextInt(1200, 1400).toLong()

        val translateY = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -8f, 0f).apply {
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
        }
        translateY.duration = Random.nextInt(1200, 1400).toLong()

        val rotation = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 0.5f, -0.5f, 0f)
        rotation.duration = Random.nextInt(1600, 1800).toLong()

        animatorSet.playTogether(scaleY, translateY, rotation)
        animatorSet.interpolator = OvershootInterpolator(0.5f)
        animatorSet.start()

        return animatorSet
    }

    fun moveWithSpringAnimation(sourceImageView: View, targetView: View, endAnimation: () -> Unit = {}) {
        sourceImageView.clearAnimation()

        val sourceLocation = IntArray(2)
        val targetLocation = IntArray(2)
        sourceImageView.getLocationOnScreen(sourceLocation)
        targetView.getLocationOnScreen(targetLocation)

        val sourceCenterX = sourceLocation[0] + sourceImageView.width / 2
        val sourceCenterY = sourceLocation[1] + sourceImageView.height / 2
        val targetCenterX = targetLocation[0] + targetView.width / 2
        val targetCenterY = targetLocation[1] + targetView.height / 2

        val deltaX = targetCenterX - sourceCenterX
        val deltaY = targetCenterY - sourceCenterY

        sourceImageView.animate()
            .translationX(deltaX.toFloat())
            .translationY(deltaY.toFloat())
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator(0.5f))
            .withEndAction {
                endAnimation.invoke()
            }
            .start()
    }

    fun moveRightAndHide(view: View) {
        view.clearAnimation()

        val displayMetrics = view.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val viewLocation = IntArray(2)
        view.getLocationOnScreen(viewLocation)
        val translationX = screenWidth - viewLocation[0] + view.width

        // Анимация
        view.animate()
            .translationX(translationX.toFloat())
            .setDuration(1000)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                view.visibility = View.INVISIBLE
                view.translationX = 0f
            }
            .start()
    }
}