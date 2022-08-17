package com.tomasfp.pokedex.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.model.extensions.getPokemonImage
import com.tomasfp.pokedex.ui.detail.DetailViewModel
import com.tomasfp.pokedex.ui.detail.StatBarLayout
import com.tomasfp.pokedex.ui.ui.theme.KindaBlue
import com.tomasfp.pokedex.ui.ui.theme.Teal200
import com.tomasfp.pokedex.ui.ui.theme.monserrat


@Composable
fun PokemonDetailScreen(
    navController: NavController,
    pokemonName: String,
    viewModel: DetailViewModel = hiltViewModel()
) {

    LaunchedEffect(Unit) {
        viewModel.getPokemonDetail(pokemonName)
    }

    val pokemonDetails = viewModel.state.collectAsState()

    when (pokemonDetails.value) {
        is DetailViewModel.State.Loading -> {}
        is DetailViewModel.State.PokemonDetail -> {
            PokemonDetailSuccess((pokemonDetails.value as DetailViewModel.State.PokemonDetail).detail)
        }
        is DetailViewModel.State.Error -> PokemonErrorScreen(
            (pokemonDetails.value as DetailViewModel.State.Error).message,
            navController
        )
    }
}

@Composable
fun PokemonErrorScreen(message: String?, navController: NavController) {
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = {
            Text(text = "Error")
        },
        text = {
            Text(message.toString())
        },
        confirmButton = {
            Button(

                onClick = {
                    navController.popBackStack()
                }) {
                Text("OK")
            }
        })
}

@Composable
fun PokemonDetailSuccess(detail: PokemonDetailResponse) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {

            PokemonImage(detail)
            PokemonName(detail)
            PokemonTypes(detail)
            Tabs(detail)


        }

}


@Composable
fun Tabs(detail: PokemonDetailResponse) {
    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf("Stats", "Features")

    TabRow(selectedTabIndex = selectedIndex,
        backgroundColor = KindaBlue,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(50))
            .padding(2.dp),
        indicator = { }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.White
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        KindaBlue
                    ),
                selected = selected,
                onClick = { selectedIndex = index },
                text = {
                    Text(
                        text = text,
                        color = Teal200,
                        fontFamily = monserrat,
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    }

    TabContent(selectedIndex, detail)
}

@Composable
fun TabContent(selectedIndex: Int, detail: PokemonDetailResponse) {
    when (selectedIndex) {
        0 -> {
            StatBarList(detail)
        }
        1 -> {
            PokemonFeatures(detail)
        }
    }
}

@Composable
fun PokemonFeatures(detail: PokemonDetailResponse) {
    Row() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Height",
                fontSize = 25.sp,
                fontFamily = monserrat,
                color = KindaBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = detail.height.toMeters(),
                fontSize = 25.sp,
                fontFamily = monserrat,
                color = KindaBlue,
                fontWeight = FontWeight.Bold
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(16.dp),
                text = "Weight",
                fontSize = 25.sp,
                fontFamily = monserrat,
                color = KindaBlue,
                fontWeight = FontWeight.Bold
            )
            Text(
                modifier = Modifier.padding(8.dp),
                text = detail.weight.toKgs(),
                fontSize = 25.sp,
                fontFamily = monserrat,
                color = KindaBlue,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun Int.toMeters(): String  = (toDouble() / 10).toString() + " m"


private fun Int.toKgs(): String = (toDouble() / 10).toString() + " kg"

@Composable
fun PokemonTypes(detail: PokemonDetailResponse) {
    Row() {
        Text(
            text = detail.types[0].type.name.uppercase(),
            modifier = Modifier
                .padding(10.dp)
                .border(3.dp, color = KindaBlue, shape = RoundedCornerShape(15.dp))
                .padding(8.dp),
            fontSize = 25.sp,
            fontFamily = monserrat,
            color = KindaBlue,
            fontWeight = FontWeight.Bold
        )

        if (detail.types.getOrNull(1) != null)
            Text(
                text = detail.types[1].type.name.uppercase(),
                modifier = Modifier
                    .padding(10.dp)
                    .border(3.dp, color = KindaBlue, shape = RoundedCornerShape(15.dp))
                    .padding(8.dp),
                fontSize = 25.sp,
                fontFamily = monserrat,
                color = KindaBlue,
                fontWeight = FontWeight.Bold
            )
    }
}

@Composable
fun PokemonName(detail: PokemonDetailResponse) {
    Text(
        text = detail.name.capitalize(Locale.current),
        color = KindaBlue,
        fontFamily = monserrat,
        fontSize = 40.sp,
        fontWeight = FontWeight.ExtraBold
    )
}

@Composable
fun PokemonImage(detail: PokemonDetailResponse) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(detail.getPokemonImage())
            .crossfade(true)
            .build(),
        placeholder = painterResource(id = R.drawable.ic_pokeball),
        contentDescription = stringResource(R.string.app_name),
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .width(250.dp)
            .height(250.dp)
    )
}

@Composable
fun StatBarList(detail: PokemonDetailResponse) {

    Column(Modifier.padding(top = 16.dp)) {
        detail.stats.forEach {
            StatBar(statName = it.stat.name.take(3).uppercase(), statValue = it.base_stat)
        }
    }
}

@Composable
fun StatBar(statName: String, statValue: Int) {
    val target = statValue.toFloat() / 100

    var progress by remember { mutableStateOf(0.0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = statName, fontFamily = monserrat, fontWeight = FontWeight.Bold)
        Text(
            text = statValue.toString(),
            fontFamily = monserrat,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
        LinearProgressIndicator(
            progress = animatedProgress, color = KindaBlue, modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        LaunchedEffect(Unit) {
            progress = target
        }
    }

}
