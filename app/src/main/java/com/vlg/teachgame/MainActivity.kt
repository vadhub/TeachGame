package com.vlg.teachgame

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.FileManager
import com.vlg.teachgame.state.LearnFragment

class MainActivity : AppCompatActivity(), Navigator, QuestionsManager {

    private lateinit var questions: List<Question>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questions = FileManager().parseQuestions(assets.open("questions.json"))
        startFragment(LearnFragment())
    }

    override fun startFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment).commit()
    }

    override fun showAd() {
        TODO("Not yet implemented")
    }

    override fun loadInterstitialAd() {
        TODO("Not yet implemented")
    }

    override fun destroyInterstitialAd() {
        TODO("Not yet implemented")
    }

    override fun get() = questions
}