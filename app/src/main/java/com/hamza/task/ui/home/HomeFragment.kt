package com.hamza.task.ui.home

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
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

@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), OnPlayerSelected, OnSelectedPlayerListener {

    private val playersViewModel: PlayersViewModel by viewModels()

    private val playersAdapter by lazy { PlayersAdapter(this) }
    private lateinit var filtersAdapter: FiltersAdapter

    private var playersList = mutableListOf<Player>()

    private var selectedPlayers: MutableList<Player> = mutableListOf()
    private var selectedPlayersAdapter = SelectedPlayersAdapter(this)


    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {

        val filters = arrayListOf("All")
        playersViewModel.players.observe(this) { players ->
            filters.addAll(players.map {
                it.team.name
            })
            filtersAdapter = FiltersAdapter() {}

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
        updatePlayersNum()

        binding.homeFragment.setOnClickListener{
            isClicked = false
            itemClickedPosition = -1
            selectedPlayersAdapter.notifyDataSetChanged()
            Log.i("hamzaH", "clicked on the home Fragment")
        }

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


    private fun updatePlayersNum() {
        if (playersNum == 15) updateAutofillText(true) else updateAutofillText(false)
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
        Log.d("hamzaHOME", "onPlayerSelected()")
        try{
            if (activePosition < 15) {
                selectedPlayers[activePosition] = player
                binding.selectedPlayersRecyclerView.smoothScrollToPosition(activePosition)

                activePosition = selectedPlayers.indexOfFirst { it.name.isBlank()  }
                currentPosition++

                currentBudget += player.marketValue
                updateBudget(currentBudget.toReadableFormat())

                playersNum++
                updatePlayersNum()

                playersList.find { it.name == player.name }!!.selected = true
                Log.d("hamzaPlayerss", "selected Players from array: ${playersList.filter { it.selected }.size}")

                selectedPlayersAdapter.differ.submitList(selectedPlayers.toList())
                selectedPlayersAdapter.notifyDataSetChanged()
                playersAdapter.notifyDataSetChanged()

                if (currentPosition < 15){
                    filterByPosition(selectedPlayers[currentPosition].position)
                }



            } else if (activePosition == 15) {
                showLongToast("You reached to the limit of the squad")
            } 
        } catch (e: IndexOutOfBoundsException){
            Log.e("hamzaError", "onPlayerSelected error: $e")
            showLongToast("You reached to the limit of the squad")
        }
       


    }

    private fun updateAutofillText(isDark: Boolean) {
        if (isDark)
        binding.autofillTextView.setBackgroundResource(R.drawable.autofills_button_background_dark)
        else binding.autofillTextView.setBackgroundResource(R.drawable.autofills_button_background)
    }

    companion object {

        var currentPosition = 0
        var selectedPosition = 0


        var activePosition = 0

        var currentBudget = 0.0

        var itemClickedPosition = -1
        var isClicked = false

        var playersNum = 0

    }

    override fun onSelectedPlayerClicked(position: Int) {
        Log.i("hamzaHOME", "onSelectedPlayerClicked()")
        itemClickedPosition = position
        isClicked = true
        filterByPosition(selectedPlayers[position].position)
        selectedPlayersAdapter.notifyDataSetChanged()

    }

    override fun onDeletedPlayerClicked(index: Int, player: Player) {
        Log.d("hamzaHOME", "onDeletedPlayerClicked()")
        playersList.find { it.name == player.name }!!.selected = false
        selectedPlayers[index] = Player(position = player.position)

        activePosition = index
        filterByPosition(player.position)
        selectedPlayersAdapter.notifyDataSetChanged()

        currentBudget -= player.marketValue
        updateBudget(currentBudget.toReadableFormat())
        playersNum--
        updatePlayersNum()
    }


}