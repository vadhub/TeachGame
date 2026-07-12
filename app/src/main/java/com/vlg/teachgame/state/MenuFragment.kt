package com.vlg.teachgame.state

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R
import com.vlg.teachgame.model.PreferenceManager

class MenuFragment : Fragment() {

    private lateinit var navigator: Navigator
    private lateinit var preferenceManager: PreferenceManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
        preferenceManager = PreferenceManager(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val petya = view.findViewById<ImageView>(R.id.petya)
        val vasya = view.findViewById<ImageView>(R.id.vasya)
        val masha = view.findViewById<ImageView>(R.id.masha)
        val ringButton = view.findViewById<View>(R.id.ringButton)

        startWalkingAnimation(petya, 400f, 2000L)
        startWalkingAnimation(vasya, 300f, 1800L)
        startWalkingAnimation(masha, 500f, 2200L)

        ringButton.setOnClickListener {
            ringButton.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    ringButton.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(100)
                        .start()

                    val isHomework = preferenceManager.isHomework()
                    if (isHomework) {
                        navigator.startFragment(HomeworkFragment())
                    } else {
                        navigator.startFragment(LearnFragment())
                    }
                }
                .start()
        }
    }

    /**
     * Запускает бесконечную анимацию ходьбы (перемещение влево-вправо)
     * @param view ImageView ученика
     * @param maxOffset максимальное смещение по X (в пикселях)
     * @param duration длительность одного цикла (туда-обратно)
     */
    private fun startWalkingAnimation(view: ImageView, maxOffset: Float, duration: Long) {
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            this.duration = duration
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val fraction = it.animatedValue as Float
                view.translationX = fraction * maxOffset
            }
        }
        animator.start()
    }
}