package com.tomasfp.pokedex.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tomasfp.pokedex.data.db.AppDB
import com.tomasfp.pokedex.data.remote.PokemonRemoteMediator
import com.tomasfp.pokedex.data.remote.PokemonService
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.model.PokemonModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val service: PokemonService, private val database: AppDB) :
    HomeRepository {

    @OptIn(ExperimentalPagingApi::class)
    override val pokemons: Flow<PagingData<PokemonModel>> =
        Pager(config = PagingConfig(
            pageSize = 20,
            prefetchDistance = 40,
            enablePlaceholders = true,
        ),
            remoteMediator = PokemonRemoteMediator(service, database),
            pagingSourceFactory = { database.pokemonDao().pagingSource() }
        ).flow

    override fun getPokemonDetail(name: String): Flow<Result<PokemonDetailResponse>> {
        return flow {
            try {
                val pokemon = service.getPokemonDetail(name)
                emit(Result.success(pokemon))
            } catch (e : Exception) {
                emit(Result.failure(e))
            }
        }
    }

    override fun searchPokemon(string: String): Flow<Result<List<PokemonModel>>> {
        return flow {
            val pokemons = service.getPokemonList(limit = 100000, offset = 0)
            val filtered = pokemons.results.filter { it.name.contains(string.lowercase()) }
            emit(Result.success(filtered))
        }
    }

    override fun getOriginalPokemons(): Flow<Result<List<PokemonModel>>> {
        return flow {
            val pokemons = service.getPokemonList(limit = 151, offset = 0)
            emit(Result.success(pokemons.results))
        }
    }
}