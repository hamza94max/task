package com.hamza.task.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hamza.task.R
import com.hamza.task.domain.models.Player
import com.hamza.task.databinding.SelectedItemPlayerCardBinding
import com.hamza.task.databinding.ActiveSelectedItemBinding
import com.hamza.task.databinding.NonActiveSelectedItemBinding
import com.hamza.task.ui.home.HomeFragment.Companion.activePosition
import com.hamza.task.ui.home.HomeFragment.Companion.isClicked
import com.hamza.task.ui.home.HomeFragment.Companion.itemClickedPosition
import com.hamza.task.utils.Ext.toReadableFormat

class SelectedPlayersAdapter(
    val onSelectedPlayerListener: OnSelectedPlayerListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // View types
    companion object {
        const val VIEW_TYPE_PLAYER = 1
        const val VIEW_TYPE_ACTIVE = 2
        const val VIEW_TYPE_NON_ACTIVE = 3

    }

    inner class PlayerViewHolder(val binding: SelectedItemPlayerCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Player) {

            data.apply {
                binding.apply {

                    Glide
                        .with(itemView.context)
                        .load(photoUrl)
                        .into(playerImageView)

                    Glide
                        .with(itemView.context)
                        .load(team.logoUrl)
                        .placeholder(R.drawable.team_ex)
                        .into(teamImageView)


                    Glide
                        .with(itemView.context)
                        .load(league.logoUrl)
                        .placeholder(R.drawable.league_ex)
                        .into(leagueImageView)

                    playerNameTextVIew.text = name
                    playerNumberTextView.text = "#$jerseyNumber"
                    playerPositionTextView.text = position.position
                    playerPositionCardTextView.text = position.position
                    playerMarketValue.text = marketValue.toReadableFormat()

                    if (isClicked) {
                        overlay.isVisible = itemClickedPosition != adapterPosition
                        closeImageView.isVisible = itemClickedPosition == adapterPosition
                    }

                    if (adapterPosition == differ.currentList.size - 1){
                        closeImageView.isVisible = true
                    }
                    binding.closeImageView.setOnClickListener {
                        onSelectedPlayerListener.onDeletedPlayerClicked(adapterPosition, differ.currentList[adapterPosition])
                    }

                }
            }

            itemView.setOnClickListener {
                if (differ.currentList[adapterPosition].name.isNotBlank()){
                    onSelectedPlayerListener.onSelectedPlayerClicked(adapterPosition)
                    notifyDataSetChanged()
                }
            }

        }


    }

    inner class ActiveViewHolder(val binding: ActiveSelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindActive(currentItem: Player) {
            binding.playerPositionTextView.text = currentItem.position.position
            binding.playerPositionTextView.setTextColor(
                ContextCompat.getColor(
                    itemView.context,
                    R.color.yellow
                )
            )
        }
    }


    inner class NonActiveViewHolder(val binding: NonActiveSelectedItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindNonActive(currentItem: Player) {
            binding.playerPositionTextView.text = currentItem.position.position
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun getItemViewType(position: Int): Int {
        val player = differ.currentList[position]
        return when {
            player.selected -> VIEW_TYPE_PLAYER
            position == activePosition -> VIEW_TYPE_ACTIVE
            else -> VIEW_TYPE_NON_ACTIVE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_PLAYER -> {
                val view = SelectedItemPlayerCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                PlayerViewHolder(view)
            }

            VIEW_TYPE_ACTIVE -> {
                val view = ActiveSelectedItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ActiveViewHolder(view)
            }

            VIEW_TYPE_NON_ACTIVE -> {
                val view = NonActiveSelectedItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                NonActiveViewHolder(view)
            }

            else -> throw IllegalArgumentException("error: Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        when (holder) {
            is PlayerViewHolder -> holder.onBind(currentItem)
            is ActiveViewHolder -> holder.bindActive(currentItem)
            is NonActiveViewHolder -> holder.bindNonActive(currentItem)
        }

        // Handle click event for active slot
        if (holder is ActiveViewHolder) {
            holder.itemView.setOnClickListener {
                if (position == activePosition) {
                    // Add player to this slot
                    //currentItem.selected = true
                    // Move active to the next slot
                    activePosition = findNextAvailableSlot()
                    notifyDataSetChanged() // Update the RecyclerView
                }
            }
        }
    }

    // Find next available slot (first unselected item)
    fun findNextAvailableSlot(): Int {
        return differ.currentList.indexOfFirst { it.name.isBlank() }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}
