package com.tomasfp.pokedex.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "pokemons")
data class PokemonModel (
    @PrimaryKey
    val name : String ,
    val url: String,
    var type: List<PokemonTypeModel>?
    ) : Parcelable {
        fun getPokemonImage() = "https://img.pokemondb.net/sprites/home/normal/${name.lowercase()}.png"
        fun cleanId() = url.dropLast(1).split("/").last()
        fun getPokemonIndex() = url.dropLast(1).split("/").last().padStart(3,'0')
        fun getPokemonIndexNoPad() = url.dropLast(1).split("/").last()

    }