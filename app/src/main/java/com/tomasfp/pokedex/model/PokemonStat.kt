package com.tomasfp.pokedex.model

data class PokemonStat (
    val base_stat: Int,
    val effort: Int,
    val stat: StatDetail
)

data class StatDetail(
    val name: String,
    val url: String
)