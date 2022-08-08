package com.tomasfp.pokedex.model

import android.content.Context
import android.os.Parcelable
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.model.PokemonTypeModel.*
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "pokemons")
data class PokemonModel (
    @PrimaryKey
    val name : String ,
    val url: String,
    var type: List<PokemonTypeModel>?
    ) : Parcelable