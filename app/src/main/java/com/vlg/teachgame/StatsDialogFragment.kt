package com.vlg.teachgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class StatsDialogFragment : DialogFragment() {

    private var correctCount: Int = 0
    private var mistakeCount: Int = 0
    private var onOkClickListener: (() -> Unit)? = null

    companion object {
        private const val ARG_CORRECT = "correct"
        private const val ARG_MISTAKES = "mistakes"

        fun newInstance(correct: Int, incorrect: Int, mistakes: Int): StatsDialogFragment {
            val fragment = StatsDialogFragment()
            val args = Bundle()
            args.putInt(ARG_CORRECT, correct)
            args.putInt(ARG_MISTAKES, mistakes)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            correctCount = it.getInt(ARG_CORRECT, 0)
            mistakeCount = it.getInt(ARG_MISTAKES, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_stats, container, false)

        view.findViewById<TextView>(R.id.correct).text = "Правильных ответов: $correctCount"
        view.findViewById<TextView>(R.id.mistakes).text = "Ошибок учителя: $mistakeCount"

        view.findViewById<Button>(R.id.okButton).setOnClickListener {
            dismiss()
            onOkClickListener?.invoke()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun setOnOkListener(listener: () -> Unit) {
        this.onOkClickListener = listener
    }
}