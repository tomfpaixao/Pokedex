package com.tomasfp.pokedex.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.FragmentHomeLayoutCollapseBinding
import com.tomasfp.pokedex.ui.home.HomeViewModel.*
import com.tomasfp.pokedex.ui.home.adapter.PokemonLoadingStateAdapter
import com.tomasfp.pokedex.ui.home.adapter.PokemonPagedAdapter
import com.tomasfp.pokedex.utils.viewBinding
import com.tomasfp.pokedex.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_layout_collapse),
    PokemonLoadingStateAdapter.RetryPressedListener {

    private val viewModel by viewModels<HomeViewModel>()

    private val binding by viewBinding<FragmentHomeLayoutCollapseBinding>()

    private lateinit var pokemonAdapter: PokemonPagedAdapter

    private lateinit var footerAdapter: PokemonLoadingStateAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
        setAdapters()

        viewModel.getPokemonsPaged()
        binding.homeRecyclerview.visible()
    }

    private fun setAdapters() {
        pokemonAdapter = PokemonPagedAdapter()
        footerAdapter = PokemonLoadingStateAdapter(this)

        with(binding.homeRecyclerview) {
            adapter = pokemonAdapter.withLoadStateFooter(footer = footerAdapter)

            (layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == pokemonAdapter.itemCount && footerAdapter.itemCount > 0) {
                            2
                        } else {
                            1
                        }
                    }
                }
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is State.Loading -> {}
                    is State.PokemonList -> {
                        pokemonAdapter.submitData(state.pokeList)
                    }
                }
            }
        }
    }

    override fun onRetryPressed() {
        pokemonAdapter.refresh()
    }
}