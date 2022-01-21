package com.tomasfp.pokedex.repository

import com.tomasfp.pokedex.data.PokemonService
import com.tomasfp.pokedex.model.PokemonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class HomeRepository @Inject constructor(private val service : PokemonService) {

    fun getPokemonList() : Flow<Result<PokemonResponse>> {
        return flow {
            try {
                val call = service.getPokemonList()
                emit(Result.success(call))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }
}