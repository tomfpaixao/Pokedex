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
    ) : Parcelable