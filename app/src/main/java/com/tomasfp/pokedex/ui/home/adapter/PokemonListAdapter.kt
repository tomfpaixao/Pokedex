package com.tomasfp.pokedex.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.PokemonListItemBinding
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.ui.home.HomeFragmentDirections

class PokemonListAdapter : ListAdapter<PokemonModel, PokemonListAdapter.ViewHolder>(PokemonDiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder = ViewHolder.create(LayoutInflater.from(parent.context), parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class ViewHolder(private val binding: PokemonListItemBinding): RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun create(layoutInflater: LayoutInflater, parent: ViewGroup?): ViewHolder {
                val crewItemBinding = PokemonListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(crewItemBinding)
            }
        }

        fun bind(pokemon: PokemonModel) {
            with(binding) {
                textViewName.text = pokemon.name
                imageView.load(pokemon.getPokemonImage()) { placeholder(R.drawable.ic_pokeball)}
                textViewID.text = pokemon.getPokemonIndex()
                root.setOnClickListener { view ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(pokemon)
                    view.findNavController().navigate(action)
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