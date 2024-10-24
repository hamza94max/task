package com.hamza.task.ui.home


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hamza.task.R
import com.hamza.task.databinding.FilterItemBinding
import com.hamza.task.ui.home.HomeFragment.Companion.selectedPosition

class FiltersAdapter (
    private val onFilterSelected: (Int) -> Unit
): RecyclerView.Adapter<FiltersAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: FilterItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener{
                selectedPosition = adapterPosition
                notifyDataSetChanged()
                onFilterSelected(adapterPosition)
            }
        }

    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FiltersAdapter.ViewHolder {
        val view =
            FilterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: FiltersAdapter.ViewHolder, position: Int) {

        val currentItem = differ.currentList[position]

        holder.binding.apply {

            filterTextView.text = currentItem

            filterTextView.background = if (position == selectedPosition){
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.filter_selected_item_background)
                } else {
                ContextCompat.getDrawable(holder.itemView.context, R.drawable.filter_not_selected_background)
            }

            filterTextView.setTextColor(
                if (position == selectedPosition){
                    ContextCompat.getColor(holder.itemView.context, R.color.white)
                } else {
                    ContextCompat.getColor(holder.itemView.context, R.color.text_color)
                }
            )


        }




    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}
