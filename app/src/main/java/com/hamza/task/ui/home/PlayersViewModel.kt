package com.hamza.task.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModel
import com.hamza.task.domain.models.Player
import com.hamza.task.domain.repo.PlayersRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayersViewModel @Inject constructor(
    private val playersRepo: PlayersRepo
) : ViewModel() {

    private val _players = MutableLiveData<List<Player>>()
    val players: LiveData<List<Player>> get() = _players

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    init {
        fetchPlayers()
    }

    private fun fetchPlayers() {
        viewModelScope.launch {
            try {
                Log.i("hamzaDATA", "fetchPlayers()")
                val playersList = playersRepo.getPlayers()
                _players.value = playersList
            } catch (e: Exception) {
                Log.e("hamzaDATAError", e.message.toString())
                _error.value = e.message
            }
        }
    }

}