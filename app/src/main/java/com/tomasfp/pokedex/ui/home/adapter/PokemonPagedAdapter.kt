package com.tomasfp.pokedex.ui.home.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.PokemonListItemBinding
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonTypeModel
import com.tomasfp.pokedex.model.PokemonTypeModel.*
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


    @SuppressLint("NewApi")
    fun bind(pokemon: PokemonModel?) {
        if (pokemon != null)
            with(binding) {
                textViewName.text = pokemon.name.replaceFirstChar { it.uppercaseChar() }
                imageView.load(pokemon.getPokemonImage()) { placeholder(R.drawable.ic_pokeball)}
                root.context.let {
                    textViewID.text = it.getString(R.string.pokemon_index_placeholder,pokemon.getPokemonIndex())
                    val bg = homeRelativeLayout.background
                    bg.setTint(it.resources.getColor(setPokemonTypeBackground(pokemon.type?.firstOrNull()),it.theme))
                    homeRelativeLayout.background = bg
                    //FIXME
                }
                root.setOnClickListener { view ->
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(pokemon)
                    view.findNavController().navigate(action)
                }
                setPokemonTypes(pokemon.type)
            }
    }

    private fun setPokemonTypeBackground(type: PokemonTypeModel?): Int {
        return when (type) {
            BUG -> R.color.bug
            DARK -> R.color.dark
            DRAGON -> R.color.dragon
            ELECTRIC -> R.color.electric
            FAIRY -> R.color.fairy
            FIGHTING -> R.color.fighting
            FIRE -> R.color.fire
            FLYING -> R.color.flying
            GHOST -> R.color.ghost
            GRASS -> R.color.grass
            GROUND -> R.color.ground
            ICE -> R.color.ice
            NORMAL ->R.color.normal
            POISON -> R.color.poison
            PSYCHIC -> R.color.psychic
            ROCK -> R.color.rock
            STEEL ->R.color.steel
            WATER -> R.color.water
            null -> R.color.white
        }
    }

    private fun setPokemonTypes(typeList: List<PokemonTypeModel>?) {
        binding.apply {
            if (typeList != null && typeList.isNotEmpty()) {
                val slot1 = typeList.getOrNull(0)
                val slot2 = typeList.getOrNull(1)

                if(slot1 != null) textViewType1.text = slot1.name else textViewType1.gone()
                if(slot2 != null) textViewType2.text = slot2.name else textViewType2.gone()

            }else {
                textViewType1.gone()
                textViewType2.gone()
            }
        }
    }
}