package com.hamza.task.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hamza.task.base.BaseFragment
import com.hamza.task.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val playersViewModel: PlayersViewModel by viewModels()

    private val playersAdapter by lazy { PlayersAdapter() }
    private lateinit var filtersAdapter: FiltersAdapter

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

            binding.playersRecyclerView.adapter = playersAdapter
            playersAdapter.differ.submitList(players)
        }

        playersViewModel.error.observe(this) {
            showLongToast(it)
        }


    }


}