package com.tomasfp.pokedex.repository

import androidx.paging.PagingData
import com.tomasfp.pokedex.model.PokemonModel
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    val pokemons: Flow<PagingData<PokemonModel>>
}