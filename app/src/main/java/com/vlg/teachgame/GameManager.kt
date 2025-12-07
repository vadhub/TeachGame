package com.vlg.teachgame

import com.vlg.teachgame.data.Question

interface GameManager {
    fun get(): List<Question>
    fun complete()
    fun increaseNumQuestion()
}