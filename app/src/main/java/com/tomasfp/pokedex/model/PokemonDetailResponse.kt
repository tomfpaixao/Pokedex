package com.tomasfp.pokedex.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PokemonDetailResponse(
    @Json(name = "height")
    val height: Int,
    @Json(name = "id")
    val id: Int,
    @Json(name = "name")
    val name: String,
    @Json(name = "order")
    val order: Int,
    @Json(name = "types")
    val types: List<PokemonTypes>,
    @Json(name = "weight")
    val weight: Int
)


fun List<PokemonTypes>.toModel(): List<PokemonTypeModel> =
    this.map { it.type.toModel() }


private fun PokemonType.toModel(): PokemonTypeModel {
   return when(this.name) {
        "bug" -> PokemonTypeModel.BUG
        "dark" -> PokemonTypeModel.DARK
        "dragon" -> PokemonTypeModel.DRAGON
        "electric" -> PokemonTypeModel.ELECTRIC
        "fairy" -> PokemonTypeModel.FAIRY
        "fighting" -> PokemonTypeModel.FIGHTING
        "fire" -> PokemonTypeModel.FIRE
        "flying" -> PokemonTypeModel.FLYING
        "ghost" -> PokemonTypeModel.GHOST
        "grass" -> PokemonTypeModel.GRASS
        "ground" -> PokemonTypeModel.GROUND
        "ice" -> PokemonTypeModel.ICE
        "normal" -> PokemonTypeModel.NORMAL
        "poison" -> PokemonTypeModel.POISON
        "psychic" -> PokemonTypeModel.PSYCHIC
        "rock" -> PokemonTypeModel.ROCK
        "steel" -> PokemonTypeModel.STEEL
        "water" -> PokemonTypeModel.WATER
       else -> PokemonTypeModel.WATER
   }
}
