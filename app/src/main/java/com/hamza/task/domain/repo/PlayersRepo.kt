package com.hamza.task.domain.repo

import com.hamza.task.domain.models.Player

interface PlayersRepo {

    suspend fun getPlayers(): List<Player>



}