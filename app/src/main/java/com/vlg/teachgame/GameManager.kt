package com.vlg.teachgame

import com.vlg.teachgame.data.Homework
import com.vlg.teachgame.data.Question

interface GameManager {
    fun getQuestions(): List<Question>
    fun getHomeworks(): List<Homework>
    fun completeLearn()
    fun completeHomeWork()
    fun increaseNumQuestion()
    fun checkTeacher(isAnswerAccuracy: Boolean, teacherReact: Boolean)
}