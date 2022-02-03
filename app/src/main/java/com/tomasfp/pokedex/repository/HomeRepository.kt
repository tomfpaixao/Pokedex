package com.tomasfp.pokedex.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tomasfp.pokedex.data.db.AppDB
import com.tomasfp.pokedex.data.remote.PokemonRemoteMediator
import com.tomasfp.pokedex.data.remote.PokemonService
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class HomeRepository @Inject constructor(private val service : PokemonService, private val database: AppDB) {

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

    @OptIn(ExperimentalPagingApi::class)
    val pokemons: Flow<PagingData<PokemonModel>> =
        Pager(config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 10,
            enablePlaceholders = true,
        ),
            remoteMediator = PokemonRemoteMediator(service,database),
            pagingSourceFactory = { database.pokemonDao().pagingSource() }
        ).flow
}