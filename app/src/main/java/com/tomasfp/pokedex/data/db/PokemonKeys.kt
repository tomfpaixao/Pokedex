package com.tomasfp.pokedex.data.db

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class PokemonKeys (
    @PrimaryKey
    @NonNull
    val name: String,
    val prevKey: Int?,
    val nextKey: Int?
)