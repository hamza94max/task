package com.hamza.task.ui.home

import com.hamza.task.domain.models.Player

interface OnSelectedPlayerListener {


    fun onSelectedPlayerClicked(position: Int)

    fun onDeletedPlayerClicked(index:Int, player: Player)



}