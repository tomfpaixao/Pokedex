package com.tomasfp.pokedex.ui.home.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.PokemonListItemBinding
import com.tomasfp.pokedex.model.*
import com.tomasfp.pokedex.model.PokemonTypeModel.*
import com.tomasfp.pokedex.model.extensions.*
import com.tomasfp.pokedex.ui.home.HomeFragmentDirections
import com.tomasfp.pokedex.utils.gone

class PokemonPagedAdapter :
    PagingDataAdapter<PokemonModel, PokemonViewHolder>(PokemonDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(
            PokemonListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class PokemonDiffCallBack : DiffUtil.ItemCallback<PokemonModel>() {
    override fun areItemsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: PokemonModel, newItem: PokemonModel): Boolean {
        return oldItem == newItem
    }
}

class PokemonViewHolder(
    private val binding: PokemonListItemBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(pokemon: PokemonModel?) {
        if (pokemon != null)
            with(binding) {
                textViewName.text = pokemon.capitalName()

                imageView.load(pokemon.getPokemonImage()) { placeholder(R.drawable.ic_pokeball)}
                root.context.let {
                    textViewID.text = it.getString(R.string.pokemon_index_placeholder,pokemon.getPokemonIndex())

                    val color = pokemon.getBackgroundColor(root.context)

                    val drawable = homeRelativeLayout.background
                    drawable.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.MULTIPLY)
                    homeRelativeLayout.background = drawable

                }
                root.setOnClickListener { view ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(pokemon)
                    view.findNavController().navigate(action)
                }

                setPokemonTypes(pokemon)
            }
    }

    private fun setPokemonTypes(pokemon: PokemonModel) {
        binding.apply {
            val leftSlot = pokemon.mainType()
            val rightSlot = pokemon.secondaryType()
            when {
                leftSlot == UNKNOWN -> {
                    textViewType1.gone()
                    textViewType2.gone()
                }
                rightSlot == UNKNOWN -> {
                    textViewType1.gone()
                    textViewType2.text = leftSlot.name
                }
                else -> {
                    textViewType2.text = leftSlot.name
                    textViewType1.text = rightSlot.name
                }
            }
        }
    }
}