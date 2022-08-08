package com.tomasfp.pokedex.model.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonTypeModel

fun PokemonModel.getPokemonImage() =
    "https://img.pokemondb.net/sprites/home/normal/${name.lowercase()}.png"

fun PokemonModel.cleanId() = url.dropLast(1).split("/").last()

fun PokemonModel.getPokemonIndex() = url.dropLast(1).split("/").last().padStart(3, '0')

fun PokemonModel.getPokemonIndexNoPad() = url.dropLast(1).split("/").last()

fun PokemonModel.mainType() = type?.first() ?: PokemonTypeModel.UNKNOWN

fun PokemonModel.secondaryType() = type?.getOrNull(1) ?: PokemonTypeModel.UNKNOWN

fun PokemonModel.capitalName() = name.replaceFirstChar { it.uppercase() }

fun PokemonTypeModel.getBackgroundColor(context: Context): Int {
    val id = when (this) {
        PokemonTypeModel.BUG -> R.color.bug
        PokemonTypeModel.DARK -> R.color.dark
        PokemonTypeModel.DRAGON -> R.color.dragon
        PokemonTypeModel.ELECTRIC -> R.color.electric
        PokemonTypeModel.FAIRY -> R.color.fairy
        PokemonTypeModel.FIGHTING -> R.color.fighting
        PokemonTypeModel.FIRE -> R.color.fire
        PokemonTypeModel.FLYING -> R.color.flying
        PokemonTypeModel.GHOST -> R.color.ghost
        PokemonTypeModel.GRASS -> R.color.grass
        PokemonTypeModel.GROUND -> R.color.ground
        PokemonTypeModel.ICE -> R.color.ice
        PokemonTypeModel.NORMAL -> R.color.normal
        PokemonTypeModel.POISON -> R.color.poison
        PokemonTypeModel.PSYCHIC -> R.color.psychic
        PokemonTypeModel.ROCK -> R.color.rock
        PokemonTypeModel.STEEL -> R.color.steel
        PokemonTypeModel.WATER -> R.color.water
        PokemonTypeModel.UNKNOWN -> R.color.white
    }
    return ContextCompat.getColor(context, id)
}

fun PokemonModel.getBackgroundColor(context: Context): Int = mainType().getBackgroundColor(context)
