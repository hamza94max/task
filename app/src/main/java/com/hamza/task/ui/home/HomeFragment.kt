package com.hamza.task.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.hamza.task.R
import com.hamza.task.base.BaseFragment
import com.hamza.task.databinding.FragmentHomeBinding
import com.hamza.task.domain.models.Player
import com.hamza.task.domain.models.Position
import com.hamza.task.utils.Constants.INIT_AVAILABLE_BUDGET
import com.hamza.task.utils.Constants.PLAYERS_NUM
import com.hamza.task.utils.Ext.toReadableFormat
import dagger.hilt.android.AndroidEntryPoint
import java.time.format.TextStyle

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnPlayerSelected, OnSelectedPlayerListener {

    private val playersViewModel: PlayersViewModel by viewModels()

    private val playersAdapter by lazy { PlayersAdapter(this) }
    private lateinit var filtersAdapter: FiltersAdapter

    private var playersList = mutableListOf<Player>()

    private var selectedPlayers = mutableListOf<Player>()
    private var selectedPlayersAdapter = SelectedPlayersAdapter(this)


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

        updateBudget("0")
        updatePlayersNum(0)

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
        playersAdapter.notifyDataSetChanged()
    }

    private fun updateBudget(budget: String) {
        val staticValue = INIT_AVAILABLE_BUDGET.toDouble().toReadableFormat()
        val fullText = "$budget / $staticValue"

        val spannable = SpannableString(fullText)

        spannable.setSpan(AbsoluteSizeSpan(16, true), 0, budget.length, 0)
        spannable.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.yellow)),
            0,
            budget.length,
            0
        )

        spannable.setSpan(AbsoluteSizeSpan(13, true), staticValue.length, fullText.length, 0)
        spannable.setSpan(ForegroundColorSpan(Color.WHITE), staticValue.length, fullText.length, 0)

        binding.budgetTextView.text = spannable

    }


    private fun updatePlayersNum(playersNum: Int) {
        val dynamicValue = playersNum.toString()
        val staticValue = "$PLAYERS_NUM"
        val fullText = "$dynamicValue / $staticValue"

        val spannable = SpannableString(fullText)

        spannable.setSpan(AbsoluteSizeSpan(16, true), 0, dynamicValue.length, 0)
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.yellow)), 0, dynamicValue.length, 0)

        spannable.setSpan(AbsoluteSizeSpan(13, true), staticValue.length, fullText.length, 0)
        spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.text_color)), staticValue.length, fullText.length, 0)

        binding.playersNumTextView.text = spannable

    }


    override fun onPlayerSelected(player: Player) {
        if (activePosition < 15) {
            selectedPlayers[activePosition] = player

            activePosition++
            currentPosition++

            currentBudget += player.marketValue
            updateBudget(currentBudget.toReadableFormat())

            updatePlayersNum(currentPosition)

            playersList.find { it.name == player.name }!!.selected = true
            Log.d("hamzaPlayerss", "selected Players from array: ${playersList.filter { it.selected }.size}")

            selectedPlayersAdapter.differ.submitList(selectedPlayers.toList())
            selectedPlayersAdapter.notifyDataSetChanged()

            if (currentPosition != 15){
                filterByPosition(selectedPlayers[currentPosition].position)
            }



        } else {
            showLongToast("You reached to the limit of the squad")
        }


    }

    companion object {

        var currentPosition = 0
        var selectedPosition = 0


        var activePosition = 0

        var currentBudget = 0.0

        var itemClickedPosition = -1
        var isClicked = false


    }

    override fun onSelectedPlayerClicked(position: Int) {
        itemClickedPosition = position
        isClicked = true
        selectedPlayersAdapter.notifyDataSetChanged()

    }


}