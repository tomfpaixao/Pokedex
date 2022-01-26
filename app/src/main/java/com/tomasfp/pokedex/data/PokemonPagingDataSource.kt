package com.tomasfp.pokedex.data

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.toModel

class PokemonPagingDataSource(private val service: PokemonService) :
    PagingSource<Int, PokemonModel>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonModel>): Int? {
        return 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
        val offset = params.key ?: 0 //TODO: default offset
        return try {
            val response = service.getPokemonList(20, offset)
            val data = response.results.map {
                val type = service.getPokemonDetail(it.name).types.toModel()
                PokemonModel(it.name,it.url,type)
            }

            var nextOffset: Int? = null

            if(response.next != null) {
                val uri = Uri.parse(response.next)
                val nextPageQuery = uri.getQueryParameter("offset")
                nextOffset = nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = data.orEmpty(),
                prevKey = null,
                nextKey = nextOffset
            )
        } catch (e: Exception) {
                LoadResult.Error(e)
        }
    }

}
