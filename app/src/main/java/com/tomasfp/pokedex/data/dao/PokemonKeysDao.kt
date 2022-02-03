package com.tomasfp.pokedex.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tomasfp.pokedex.data.db.PokemonKeys

@Dao
interface RedditKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(remoteKey: List<PokemonKeys>)

    @Query("SELECT * FROM remote_keys WHERE name = :name")
    fun remoteKeyByPokemonName(name: String?): PokemonKeys?

    @Query("DELETE FROM remote_keys")
    fun clearRemoteKeys()
}