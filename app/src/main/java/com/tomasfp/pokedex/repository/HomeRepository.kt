package com.tomasfp.pokedex.repository

import androidx.paging.PagingData
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.model.PokemonModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val pokemons: Flow<PagingData<PokemonModel>>
    fun getPokemonDetail(name: String): Flow<Result<PokemonDetailResponse>>
    fun searchPokemon(string: String): Flow<Result<List<PokemonModel>>>
    fun getOriginalPokemons(): Flow<Result<List<PokemonModel>>>
}