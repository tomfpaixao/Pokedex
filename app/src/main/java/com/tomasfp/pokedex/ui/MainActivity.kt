package com.tomasfp.pokedex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tomasfp.pokedex.databinding.ActivityMainBinding
import com.tomasfp.pokedex.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setupBottomNavigationBar(binding)
    }

    private fun setupBottomNavigationBar(binding: ActivityMainBinding) {
        val navController = binding.navHostFragment.getFragment<NavHostFragment>().navController
        navController.let { binding.bottomNav.setupWithNavController(it) }
        binding.bottomNav.itemIconTintList = null
    }
}