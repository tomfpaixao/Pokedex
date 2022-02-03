package com.tomasfp.pokedex.data.remote

import android.net.Uri
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tomasfp.pokedex.data.db.AppDB
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.toModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
    private val service: PokemonService,
    private val db: AppDB
) : RemoteMediator<Int, PokemonModel>() {

    private val pokemonDao = db.pokemonDao()


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonModel>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> state.lastItemOrNull()?.cleanId()?.toInt() ?: 0
            }

            val response = service.getPokemonList(limit = 20, offset = offset)
            val pokemons = response.results.map {
                val type = service.getPokemonDetail(it.name).types.toModel()
                PokemonModel(it.name,it.url,type)
            }

            db.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    pokemonDao.clearAll()
                }
                pokemons.let { pokemonDao.insertAll(pokemons)  }

            }

            MediatorResult.Success(endOfPaginationReached = response.next == null)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}