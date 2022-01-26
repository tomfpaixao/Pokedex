package com.tomasfp.pokedex.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tomasfp.pokedex.data.PokemonPagingDataSource
import com.tomasfp.pokedex.data.PokemonService
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class HomeRepository @Inject constructor(private val service : PokemonService) {

    fun getPokemonList() : Flow<Result<PokemonResponse>> {
        return flow {
            try {
                val call = service.getPokemonList(limit = 20, offset = 20)
                emit(Result.success(call))
            } catch (e: Exception) {
                emit(Result.failure(e))
            }
        }
    }

    val pokemons: Flow<PagingData<PokemonModel>> =
        Pager(config = PagingConfig(pageSize = 20, prefetchDistance = 10, enablePlaceholders = true),
            pagingSourceFactory = { PokemonPagingDataSource(service) }
        ).flow
}