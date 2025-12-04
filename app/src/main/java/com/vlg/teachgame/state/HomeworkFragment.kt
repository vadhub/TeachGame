package com.vlg.teachgame.state

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vlg.teachgame.Navigator
import com.vlg.teachgame.R

class HomeworkFragment: Fragment() {

    private lateinit var navigator: Navigator

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
    }
}