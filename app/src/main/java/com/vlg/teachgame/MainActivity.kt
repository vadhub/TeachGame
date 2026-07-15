package com.vlg.teachgame

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.vlg.teachgame.data.CreatedHomework
import com.vlg.teachgame.data.Homework
import com.vlg.teachgame.data.Question
import com.vlg.teachgame.model.FileManager
import com.vlg.teachgame.model.Management
import com.vlg.teachgame.model.PreferenceManager
import com.vlg.teachgame.state.HomeworkFragment
import com.vlg.teachgame.state.LearnFragment
import com.vlg.teachgame.state.MenuFragment
import kotlin.collections.addAll
import kotlin.text.clear

class MainActivity : AppCompatActivity(), Navigator, GameManager {

    private lateinit var management: Management
    private lateinit var questions: List<Question>
    private lateinit var homeworks: List<Homework>
    private val createdHomeworks = mutableListOf<CreatedHomework>()
    private var numOfQuestion: Int = 0
    private lateinit var preferenceManager: PreferenceManager
    private val fileManager = FileManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        management = Management()
        questions = fileManager.parseQuestions(assets.open("questions.json"))
        homeworks = fileManager.parseHomeworks(assets.open("homework.json"))
        preferenceManager = PreferenceManager(this)
        val list = fileManager.loadCreatedHomeworks(preferenceManager.getCreatedHomeworks())
        createdHomeworks.clear()
        createdHomeworks.addAll(list)
        startFragment(MenuFragment())
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
    override fun getHomeworks(): List<Homework>{
        val builtIn = homeworks
        val created = createdHomeworks.map {
            Homework(
                text = it.question,
                options = it.options,
                correctIndices = it.correctIndices,
                imageUri = it.imageUri
            )
        }
        return builtIn + created
    }

    override fun completeLearn() {
        showStatsDialog {
            preferenceManager.saveState(true)
            startFragment(MenuFragment())
            management.reset()
        }
    }

    override fun completeHomeWork() {
        showStatsDialog {
            preferenceManager.saveState(false)
            startFragment(MenuFragment())
            management.reset()
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

    override fun addCreatedHomework(homework: CreatedHomework) {
        createdHomeworks.add(homework)
        preferenceManager.saveCreatedHomeworks(fileManager.saveCreatedHomeworks(createdHomeworks))
    }

    override fun getCreatedHomeworks(): List<CreatedHomework> = createdHomeworks

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