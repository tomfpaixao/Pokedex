package com.tomasfp.pokedex.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.ActivityMainBinding
import com.tomasfp.pokedex.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setSupportActionBar(binding?.mainToolbar)
    }
}