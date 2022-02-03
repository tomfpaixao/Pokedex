package com.tomasfp.pokedex.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.tomasfp.pokedex.model.PokemonTypeModel

class Converters {

    @TypeConverter
    fun toPokemonType(value: String): List<PokemonTypeModel>? {
        return value.split(",").map { enumValueOf(it) }
    }

    @TypeConverter
    fun fromPokemonType(value: List<PokemonTypeModel>): String {
        val list = value.map { pokemon -> PokemonTypeModel.valueOf(pokemon.name).name }
        return list.joinToString(separator = ",")
    }
}