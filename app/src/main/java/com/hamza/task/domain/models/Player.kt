package com.hamza.task.domain.models

data class Player(
    val name: String = "",
    val position: Position = Position.FORWARD,
    val age: Int = 0,
    val nationality: Nationality = Nationality("",""),
    val photoUrl: String= "",
    val jerseyNumber: Int = 0,
    val rating: Float = 0f,
    val marketValue: Double = 0.0,
    val team: Team = Team("",""),
    val league: League = League("",""),
    val selected: Boolean = false
)

enum class Position(val position: String){
    GOALKEEPER("GK"),
    DEFENDER("DEF"),
    MIDFIELDER("MID"),
    FORWARD("FWD")
}


data class Nationality(
    val name: String = "",
    val flagUrl: String = ""
)

data class Team(
    val name: String = "",
    val logoUrl: String = ""
)

data class League(
    val name: String = "",
    val logoUrl: String = ""
)