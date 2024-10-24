package com.hamza.task.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.hamza.task.base.BaseFragment
import com.hamza.task.databinding.FragmentHomeBinding
import com.hamza.task.domain.models.Player
import com.hamza.task.domain.models.Position
import com.hamza.task.ui.home.SelectedPlayersAdapter.Companion
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnPlayerSelected {

    private val playersViewModel: PlayersViewModel by viewModels()

    private val playersAdapter by lazy { PlayersAdapter(this) }
    private lateinit var filtersAdapter: FiltersAdapter

    private var playersList = mutableListOf<Player>()

    private var selectedPlayers = mutableListOf<Player>()
    private val selectedPlayersAdapter by lazy { SelectedPlayersAdapter() }


    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {

        val filters = arrayListOf("All")
        playersViewModel.players.observe(this) { players ->
            filters.addAll(players.map {
                it.team.name
            })
            filtersAdapter = FiltersAdapter() {
//                if (it == 0){
//                    playersAdapter.differ.submitList(players)
//                } else {
//                    playersAdapter.differ.submitList(players.filter {
//                        it.team.name == filters[it]
//            }
            }
            binding.filtersRecyclerView.adapter = filtersAdapter
            filtersAdapter.differ.submitList(filters.distinct())

            Log.d("hamzaDATA", "$players")
            binding.playersRecyclerView.adapter = playersAdapter
            playersList = players.toMutableList()
            filterByPosition(selectedPlayers[currentPosition].position)
            binding.loadingProgressBar.isVisible = false
        }

        playersViewModel.error.observe(this) {
            showLongToast(it)
        }

        initSelectedPlayersList()


    }

    private fun initSelectedPlayersList() {
        repeat(2) { selectedPlayers.add(Player(position = Position.GOALKEEPER)) }
        repeat(5) { selectedPlayers.add(Player(position = Position.DEFENDER)) }
        repeat(5) { selectedPlayers.add(Player(position = Position.MIDFIELDER)) }
        repeat(3) { selectedPlayers.add(Player(position = Position.FORWARD)) }

        binding.selectedPlayersRecyclerView.adapter = selectedPlayersAdapter
        selectedPlayersAdapter.differ.submitList(selectedPlayers)
    }


    private fun filterByPosition(position: Position) {
        val filteredPlayers = playersList.filter { it.position == position }
        playersAdapter.differ.submitList(filteredPlayers)
    }


    override fun onPlayerSelected(player: Player) {
        if (activePosition < 15) {
            selectedPlayers[activePosition] = player
            activePosition++
            currentPosition++

            selectedPlayersAdapter.differ.submitList(selectedPlayers.toList())
            selectedPlayersAdapter.notifyDataSetChanged()

            if (currentPosition != 15){
                filterByPosition(selectedPlayers[currentPosition].position)
            }

        }


    }

    companion object {

        var currentPosition = 0
        var selectedPosition = 0


        var activePosition = 0


    }


}