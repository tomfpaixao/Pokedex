package com.tomasfp.pokedex.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.extensions.capitalName
import com.tomasfp.pokedex.model.extensions.getBackgroundColor
import com.tomasfp.pokedex.model.extensions.getPokemonImage
import com.tomasfp.pokedex.model.extensions.getPokemonIndex
import com.tomasfp.pokedex.ui.home.HomeViewModel
import com.tomasfp.pokedex.ui.ui.theme.KindaBlue
import com.tomasfp.pokedex.ui.ui.theme.monserrat
import kotlinx.coroutines.flow.Flow

@Composable
fun PokeDexScreen(homeViewModel: HomeViewModel, navController: NavController) {
    Box(modifier = Modifier.background(Color.White)) {
        Column() {
            PokemonList(pokemons = homeViewModel.pokemons, navController)
        }
    }
}

@Composable
private fun PokeDexSearchCollapsingBar() {

    Row(Modifier.padding(20.dp)) {
        Column (modifier = Modifier.align(CenterVertically)){
            Text(text = "Pokedex", fontSize = 40.sp, fontFamily = monserrat, color = KindaBlue, fontWeight = FontWeight.Bold)
            Text(
                text = stringResource(id = R.string.search_title), fontSize = 20.sp, fontFamily = monserrat, color = KindaBlue
            )
            PokeDexSearchBox()
        }
    }
}

@Composable
fun PokeDexSearchBox() {
    Column (Modifier.padding(top = 20.dp)) {
        var textState by remember { mutableStateOf("") }
        val lightBlue = Color(0xffd8e6ff)
        val blue = Color(0xff76a9ff)
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = textState,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = lightBlue,
                cursorColor = Color.Black,
                disabledLabelColor = lightBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            placeholder = { Text(text = "Name or number", color = KindaBlue)},
            onValueChange = {
                textState = it
            },
            shape = RoundedCornerShape(8.dp),
            singleLine = true,
            trailingIcon = {
                if (textState.isNotEmpty()) {
                    IconButton(onClick = { textState = "" }) {
                        Icon(
                            imageVector = Icons.Outlined.Close,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }
}

@Composable
fun PokemonList(pokemons: Flow<PagingData<PokemonModel>>, navController: NavController) {
        val lazyPokemonItems = pokemons.collectAsLazyPagingItems()
        LazyVerticalGrid(columns = GridCells.Fixed(2), content = {
            item (span =  { GridItemSpan(2)} ) { PokeDexSearchCollapsingBar() }
            items(lazyPokemonItems.itemCount) { index ->

                lazyPokemonItems[index]?.let { pokemon ->
                    PokemonListItem(pokemon) { navController.navigate("detail_screen/${it.name}") }
                }
            }
        })

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonListItem(pokemonModel: PokemonModel, onClick : (PokemonModel) -> Unit) {
    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(260.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 5.dp,
        backgroundColor = Color(pokemonModel.getBackgroundColor(LocalContext.current)),
        onClick =  { onClick(pokemonModel) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(pokemonModel.getPokemonImage())
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(id = R.drawable.ic_pokeball),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(140.dp)
                    .height(140.dp)
            )

            Text(text = pokemonModel.capitalName(), fontSize = 20.sp, fontFamily = monserrat, color = KindaBlue)
            Text(
                text = pokemonModel.getPokemonIndex(),
                fontSize = 18.sp,
                modifier = Modifier.alpha(0.5f), fontFamily = monserrat, color = KindaBlue
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PokedexScreenPreview() {
    //PokeDexScreen()
}

