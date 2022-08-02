package com.tomasfp.pokedex.ui.fusion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tomasfp.pokedex.databinding.PokemonSelectorItemBinding
import com.tomasfp.pokedex.model.PokemonModel

class PokemonSelectedAdapter(private val idView: Int) : ListAdapter<PokemonModel, PokemonSelectedAdapter.ViewHolder>(
    PokemonDiffCallback()
) {

    var onItemClick: ((PokemonModel,Int) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
            val binding = PokemonSelectorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: PokemonSelectorItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonModel) {
            with(binding) {
                pokemonSelectorName.text = pokemon.name
                root.setOnClickListener {
                    onItemClick?.invoke(pokemon,idView)
                }
            }
        }
    }

    private class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonModel>() {
        override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean =
            oldItem.name == newItem.name
    }
}