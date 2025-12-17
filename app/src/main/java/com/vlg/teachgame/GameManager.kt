package com.vlg.teachgame

import com.vlg.teachgame.data.Homework
import com.vlg.teachgame.data.Question

interface GameManager {
    fun getQuestions(): List<Question>
    fun getHomeworks(): List<Homework>
    fun complete()
    fun increaseNumQuestion()
}