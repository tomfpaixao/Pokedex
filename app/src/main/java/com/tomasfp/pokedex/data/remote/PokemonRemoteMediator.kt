package com.tomasfp.pokedex.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tomasfp.pokedex.data.db.AppDB
import com.tomasfp.pokedex.data.db.PokemonKeys
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.toModel
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
    private val service: PokemonService,
    private val db: AppDB
) : RemoteMediator<Int, PokemonModel>() {

    private val pokemonDao = db.pokemonDao()
    private val keysDao = db.pokemonKeysDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonModel>
    ): MediatorResult {
        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val keys = getClosestKey(state)
                    keys?.nextKey?.minus(1) ?: 0
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val remoteKeys = getLastItemKey(state)
                    when {
                        remoteKeys == null -> 0
                        remoteKeys.nextKey == null -> return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                        else -> remoteKeys.nextKey
                    }
                }
            }

            val response = service.getPokemonList(
                limit = state.config.pageSize,
                offset = page * state.config.pageSize
            )
            val pokemons = response.results.map {
                val type = service.getPokemonDetail(it.name).types.toModel()
                PokemonModel(it.name, it.url, type)
            }

            val endOfPaginationReached = response.next == null

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDao.clearAll()
                    keysDao.clearRemoteKeys()
                }


                val prevKey = if (page == 0) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = pokemons.map {
                    PokemonKeys(
                        name = it.name,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                pokemonDao.insertAll(pokemons)
                keysDao.insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }

    }

    private suspend fun getLastItemKey(state: PagingState<Int, PokemonModel>): PokemonKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            .let { pokemon ->
                db.withTransaction {
                    keysDao.remoteKeyByPokemonName(pokemon?.name)
                }
            }
    }

    private suspend fun getClosestKey(state: PagingState<Int, PokemonModel>): PokemonKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.name?.let { name ->
                db.withTransaction {
                    keysDao.remoteKeyByPokemonName(name)
                }
            }
        }

    }

    override suspend fun initialize() = InitializeAction.SKIP_INITIAL_REFRESH
}