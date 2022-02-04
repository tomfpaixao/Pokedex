package com.tomasfp.pokedex.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tomasfp.pokedex.model.PokemonModel

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonModel>)

    @Query("SELECT Count(name) from pokemons")
    suspend fun getPokemonCount(): Int

    @Query("SELECT * FROM pokemons")
    fun pagingSource(): PagingSource<Int,PokemonModel>

    @Query("DELETE FROM pokemons")
    suspend fun clearAll()


}