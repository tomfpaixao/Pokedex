package com.tomasfp.pokedex.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.FragmentHomeLayoutBinding
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.ui.home.HomeViewModel.*
import com.tomasfp.pokedex.ui.home.adapter.PokemonListAdapter
import com.tomasfp.pokedex.ui.home.adapter.PokemonPagedAdapter
import com.tomasfp.pokedex.utils.gone
import com.tomasfp.pokedex.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home_layout) {

    private val viewModel by viewModels<HomeViewModel>()

    private var binding: FragmentHomeLayoutBinding? = null

    private lateinit var adapter: PokemonPagedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeLayoutBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonPagedAdapter()
        binding?.homeRecyclerview?.adapter = adapter
        setObservers()

        viewModel.getPokemonList()
        binding?.homeRecyclerview?.visible()
    }

    private fun setObservers() {
        /*
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when(state) {
                    is State.Error -> handleError(state.error)
                    is State.Idle -> binding?.loading?.gone()
                    is State.Loading -> binding?.loading?.visible()
                    is State.Success -> onSuccess(state.pokemonList)
                }
            }
        }

         */

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getPokemonsPaged().collectLatest { pokemons ->
                adapter.submitData(pokemons)
            }
        }
    }

    /*
    private fun onSuccess(pokemonList: List<PokemonModel>) {
        binding?.homeRecyclerview?.visible()
        adapter.submitList(pokemonList)
    }

     */

    private fun handleError(error: String?) {
        Toast.makeText(activity, "An error occurred: $error",Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}