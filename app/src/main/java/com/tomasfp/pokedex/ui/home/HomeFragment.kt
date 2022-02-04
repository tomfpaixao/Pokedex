package com.tomasfp.pokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.FragmentHomeLayoutBinding
import com.tomasfp.pokedex.databinding.FragmentHomeLayoutCollapseBinding
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.ui.home.HomeViewModel.*
import com.tomasfp.pokedex.ui.home.adapter.PokemonListAdapter
import com.tomasfp.pokedex.ui.home.adapter.PokemonLoadingStateAdapter
import com.tomasfp.pokedex.ui.home.adapter.PokemonPagedAdapter
import com.tomasfp.pokedex.utils.gone
import com.tomasfp.pokedex.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_layout_collapse),
    PokemonLoadingStateAdapter.RetryPressedListener {

    private val viewModel by viewModels<HomeViewModel>()

    private var binding: FragmentHomeLayoutCollapseBinding? = null

    private lateinit var adapter: PokemonPagedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeLayoutCollapseBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonPagedAdapter()
        val footerAdapter = PokemonLoadingStateAdapter(this)

        binding?.homeRecyclerview?.adapter = adapter.withLoadStateFooter(footer = footerAdapter)

        (binding?.homeRecyclerview?.layoutManager as GridLayoutManager).spanSizeLookup =  object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount  && footerAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }

        setObservers()

        viewModel.getPokemonsPaged()
        binding?.homeRecyclerview?.visible()
    }

    private fun setObservers() {

        /*
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPokemonsPaged().collectLatest { pokemons ->
                adapter.submitData(pokemons)
            }
        }

         */

        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is State.Loading -> {
                    }
                    is State.PokemonList -> {
                        adapter.submitData(state.pokeList)
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onRetryPressed() {
        adapter.refresh()
    }
}