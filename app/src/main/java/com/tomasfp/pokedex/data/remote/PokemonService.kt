package com.tomasfp.pokedex.data.remote

import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.model.PokemonResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit : Int , @Query("offset") offset: Int) : PokemonResponse

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(@Path("name") name: String) : PokemonDetailResponse
}