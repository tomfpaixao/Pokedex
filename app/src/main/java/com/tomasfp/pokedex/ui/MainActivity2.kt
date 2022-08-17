package com.tomasfp.pokedex.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.colorspace.WhitePoint
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.ui.ui.theme.PokedexTheme
import com.tomasfp.pokedex.ui.ui.theme.Teal200
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.ui.detail.DetailViewModel
import com.tomasfp.pokedex.ui.home.HomeViewModel
import com.tomasfp.pokedex.ui.ui.theme.KindaBlue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity2 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            PokedexTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        content = { padding -> // We have to pass the scaffold inner padding to our content. That's why we use Box.
            Box(modifier = Modifier.padding(padding)) {
                Navigation(navController)
            }
        },
        backgroundColor = Color.White // Set background color to avoid the white flashing when you switch between screens
    )
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Pokedex.screenRoute) {
        composable(BottomNavItem.Pokedex.screenRoute) {
            val homeViewModel = hiltViewModel<HomeViewModel>()
            PokeDexScreen(homeViewModel = homeViewModel, navController)
        }
        composable(BottomNavItem.PokeFusion.screenRoute) {
            PokeFusionScreen()
        }
        composable("detail_screen/{pokemonName}") { backStackEntry ->
            val detailViewModel = hiltViewModel<DetailViewModel>()
            val pokemonId = backStackEntry.arguments?.getString("pokemonName")
            requireNotNull(pokemonId) { "pokemonName not found" }
            PokemonDetailScreen(navController, pokemonId,detailViewModel)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}

@Composable
fun TopBar() {
    TopAppBar(
        title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
        backgroundColor = Teal200,
        contentColor = Color.White
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview() {
    TopBar()
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Pokedex,
        BottomNavItem.PokeFusion
    )
    BottomNavigation(
        backgroundColor = KindaBlue,
        contentColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painterResource(id = item.icon),
                        contentDescription = item.title,
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(text = item.title) },
                selectedContentColor = Teal200,
                unselectedContentColor = Color.White,
                selected = false,
                alwaysShowLabel = true,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }

                        // Avoid multiple copies of the same destination when
                        // reselecting the same item
                        launchSingleTop = true
                        // Restore state when reselecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    //BottomNavigationBar()
}
