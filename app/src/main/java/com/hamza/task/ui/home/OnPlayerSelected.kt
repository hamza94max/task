package com.hamza.task.ui.home

import com.hamza.task.domain.models.Player

interface OnPlayerSelected {

    fun onPlayerSelected(player: Player)
}