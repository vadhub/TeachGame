package com.vlg.teachgame.state

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R
import com.vlg.teachgame.model.Alive

class HomeworkFragment : Fragment() {

    private lateinit var navigator: Navigator
    private var countStudents = 3

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

        nextButton.setOnClickListener {
            when (countStudents) {
                3 -> {
                    animatorSet2.cancel()
                    Alive().moveWithSpringAnimation(vasya, verticalBoard) {

                    }
                }

                2 -> {
                    animatorSet3.cancel()
                    Alive().moveWithSpringAnimation(masha, verticalBoard)
                }

                1 -> {
                    animatorSet1.cancel()
                    Alive().moveWithSpringAnimation(petya, verticalBoard)
                }
            }

            countStudents--
        }

        correctButton.setOnClickListener {
            moveRight(vasya, masha, petya)
        }
        incorrectButton.setOnClickListener {
            moveRight(vasya, masha, petya)
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
}