package com.tomasfp.pokedex.data

import com.tomasfp.pokedex.model.PokemonResponse
import retrofit2.http.GET

interface PokemonService {

    @GET("pokemon?limit=100")
    suspend fun getPokemonList() : PokemonResponse
}