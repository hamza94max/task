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

    override val bindLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding
        get() = FragmentHomeBinding::inflate

    override fun prepareView(savedInstanceState: Bundle?) {

        playersViewModel.players.observe(this) { players ->


            binding.playersRecyclerView.adapter = playersAdapter
            playersAdapter.differ.submitList(players)
        }

        playersViewModel.error.observe(this) {
            showLongToast(it)
        }


    }


}