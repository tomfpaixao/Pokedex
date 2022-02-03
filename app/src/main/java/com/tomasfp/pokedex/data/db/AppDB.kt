package com.tomasfp.pokedex.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tomasfp.pokedex.data.dao.PokemonDao
import com.tomasfp.pokedex.model.PokemonModel
import javax.inject.Singleton

@Database(entities = [PokemonModel::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
@Singleton
abstract class AppDB: RoomDatabase() {
    abstract fun pokemonDao() : PokemonDao
    }