package com.tomasfp.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonModel (
    val name : String ,
    val url: String
    ) : Parcelable {
        fun getPokemonImage() = "https://img.pokemondb.net/sprites/home/normal/${name.lowercase()}.png"

        fun getPokemonIndex() = url.dropLast(1).takeLast(1).padStart(3,'0')

    }