package com.vlg.teachgame.model

class Management {
    private var mistakesTeacher = 0
    private var correctOfTeacher = 0

    fun check(isAnswerAccuracy: Boolean, teacherReact: Boolean) {
        if (isAnswerAccuracy == teacherReact) {
            correctOfTeacher++
        } else {
            mistakesTeacher++
        }
    }

    fun getMistakesTeacher() = mistakesTeacher
    fun getCorrectOfTeacher() = correctOfTeacher
}