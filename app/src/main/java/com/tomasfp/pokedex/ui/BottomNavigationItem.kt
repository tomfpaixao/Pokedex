package com.tomasfp.pokedex.ui

import com.tomasfp.pokedex.R

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {

    object Pokedex : BottomNavItem("Pokedex", R.drawable.ic_pokeball, "pokedex")
    object PokeFusion : BottomNavItem("PokeFusion", R.drawable.ic_pikachu_logo, "pokefusion")
}