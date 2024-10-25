package com.hamza.task.ui.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hamza.task.R
import com.hamza.task.databinding.PlayerMainCardBinding
import com.hamza.task.domain.models.Player
import com.hamza.task.ui.home.listener.OnPlayerSelected
import com.hamza.task.utils.Ext.toReadableFormat

class PlayersAdapter (
    val onPlayerSelected: OnPlayerSelected
) : RecyclerView.Adapter<PlayersAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: PlayerMainCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Player) {
            data.apply {
                binding.playerCard1.apply {

                   Glide
                       .with(itemView.context)
                       .load(photoUrl)
                       .into(playerImageView)

                    Glide
                        .with(itemView.context)
                        .load(nationality.flagUrl)
                        .placeholder(R.drawable.brazil_flag)
                        .into(nationalityImageView)

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
                   nationalityTextVIew.text = nationality.name

                   selectedTextView.isVisible = selected
                   selectedOverlay.isVisible = selected
                }

                binding.playerCard2.apply {
                    playerRating.text = rating.toString()
                    playerMarketValue.text = marketValue.toReadableFormat()

                    if (selected) {
                        playerMarketValue.setBackgroundResource(R.drawable.price_blue_background)
                        playerMarketValue.setTextColor(itemView.context.getColor(R.color.green))
                    }
                }
            }
        }

        init {
            itemView.setOnClickListener{
                itemView.elevation = 12f
                if (adapterPosition != -1)
                onPlayerSelected.onPlayerSelected(differ.currentList[adapterPosition])
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<Player>() {
        override fun areItemsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Player, newItem: Player): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayersAdapter.ViewHolder {
        val view = PlayerMainCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayersAdapter.ViewHolder, position: Int) {

        val currentItem = differ.currentList[position]
        holder.onBind(currentItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
