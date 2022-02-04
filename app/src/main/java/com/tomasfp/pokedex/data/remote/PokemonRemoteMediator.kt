package com.tomasfp.pokedex.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tomasfp.pokedex.data.db.AppDB
import com.tomasfp.pokedex.data.db.PokemonKeys
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.toModel
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException
import java.lang.Exception
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
        Log.d("PokedexDB", "LOADTYPE" + loadType.name)
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val keys = getClosestKey(state)
                    keys?.nextKey?.minus(1) ?: 0
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    try {
                    val remoteKeys = getLastItemKey(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(true)
                    } catch (e: InvalidObjectException) {
                        return MediatorResult.Error(e)
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
                Log.d("PokedexDB", "Inserting pokemons" + pokemons.size)
                keysDao.insertAll(keys)
                Log.d("PokedexDB", "Inserting keys" + "First k: " + keys.first().name + " " + keys.first().prevKey + keys.first().nextKey )
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e :Exception) {
            return MediatorResult.Error(e)
        }

    }

    private suspend fun callPokemonService(
        state: PagingState<Int, PokemonModel>,
        page: Int
    ): List<PokemonModel> {
        val response = service.getPokemonList(
            limit = state.config.pageSize,
            offset = page * state.config.pageSize
        )
        val pokemons = response.results.map {
            val type = service.getPokemonDetail(it.name).types.toModel()
            PokemonModel(it.name, it.url, type)
        }
        return pokemons
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

    override suspend fun initialize(): InitializeAction {
        return if (pokemonDao.getPokemonCount() > 0) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }
}