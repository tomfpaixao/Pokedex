package com.tomasfp.pokedex.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tomasfp.pokedex.model.PokemonModel

class PokemonPagingDataSource(private val service: PokemonService) :
    PagingSource<Int, PokemonModel>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonModel>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonModel> {
        TODO("Not yet implemented")
    }

}
