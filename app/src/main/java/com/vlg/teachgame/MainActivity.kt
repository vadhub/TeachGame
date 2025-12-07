package com.vlg.teachgame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.FileManager
import com.vlg.teachgame.state.HomeworkFragment
import com.vlg.teachgame.state.LearnFragment

class MainActivity : AppCompatActivity(), Navigator, GameManager {

    private lateinit var questions: List<Question>
    private var numOfQuestion: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        questions = FileManager().parseQuestions(assets.open("questions.json"))
        startFragment(HomeworkFragment())
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

    override fun complete() {
        Log.d("!!!", "complete")
        startFragment(HomeworkFragment())
    }

    override fun increaseNumQuestion() {
        numOfQuestion++
        if (numOfQuestion > 6) {
            complete()
        }
    }
}