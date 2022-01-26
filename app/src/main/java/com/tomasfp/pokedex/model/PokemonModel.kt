package com.tomasfp.pokedex.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PokemonModel (
    val name : String ,
    val url: String,
    var type: List<PokemonTypeModel>?
    ) : Parcelable {
        fun getPokemonImage() = "https://img.pokemondb.net/sprites/home/normal/${name.lowercase()}.png"

        fun getPokemonIndex() = url.dropLast(1).split("/").last().padStart(3,'0')

    }