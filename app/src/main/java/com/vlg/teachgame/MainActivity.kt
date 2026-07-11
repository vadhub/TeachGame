package com.vlg.teachgame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vlg.teachgame.data.Homework
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.FileManager
import com.vlg.teachgame.model.Management
import com.vlg.teachgame.model.PreferenceManager
import com.vlg.teachgame.state.HomeworkFragment
import com.vlg.teachgame.state.LearnFragment

class MainActivity : AppCompatActivity(), Navigator, GameManager {

    private lateinit var management: Management
    private lateinit var questions: List<Question>
    private lateinit var homeworks: List<Homework>
    private var numOfQuestion: Int = 0
    private lateinit var preferenceManager: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        management = Management()
        questions = FileManager().parseQuestions(assets.open("questions.json"))
        homeworks = FileManager().parseHomeworks(assets.open("homework.json"))
        preferenceManager = PreferenceManager(this)
        startFragment(if (preferenceManager.isHomework()) HomeworkFragment() else LearnFragment())
    }

    override fun startFragment(fragment: Fragment) {
        Log.d("!!!", fragment.javaClass.name)
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

    override fun getQuestions() = questions
    override fun getHomeworks() = homeworks

    override fun completeLearn() {
        preferenceManager.saveState(true)
        startFragment(HomeworkFragment())
    }

    override fun completeHomeWork() {
        showStatsDialog {
            preferenceManager.saveState(false)
            startFragment(LearnFragment())
        }
    }

    override fun increaseNumQuestion() {
        numOfQuestion++
        if (numOfQuestion > 6) {
            completeLearn()
        }
    }

    override fun checkTeacher(isAnswerAccuracy: Boolean, teacherReact: Boolean) {
        management.check(isAnswerAccuracy, teacherReact)
    }

    private fun showStatsDialog(onComplete: () -> Unit) {
        val dialog = StatsDialogFragment.newInstance(
            correct = management.getCorrectOfTeacher(),
            mistakes = management.getMistakesTeacher()
        )
        dialog.setOnOkListener {
            onComplete.invoke()
        }
        dialog.show(supportFragmentManager, "stats_dialog")
    }

}