package com.tomasfp.pokedex.ui.fusion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.PokemonSelectorBottomSheetBinding
import com.tomasfp.pokedex.utils.gone
import com.tomasfp.pokedex.utils.viewBinding
import com.tomasfp.pokedex.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonSelectorBottomSheet(private val idView : Int): BottomSheetDialogFragment() {

    private val viewModel by activityViewModels<PokeFusionViewModel>()

    private lateinit var adapter: PokemonSelectedAdapter

    private val binding by viewBinding<PokemonSelectorBottomSheetBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pokemon_selector_bottom_sheet,container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PokemonSelectedAdapter(idView)
        binding.pokemonSelectorList.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        binding.pokemonSelectorList.adapter = adapter
        viewModel.getPokemons()
        setObservers()
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.state.collectLatest { state ->
                when (state) {
                    is PokeFusionViewModel.State.Loading -> {binding.loadingAnim.visible()}
                    is PokeFusionViewModel.State.PokemonList -> {
                        binding.loadingAnim.gone()
                        adapter.submitList(state.pokeList)
                    }
                }
            }
        }

        adapter.onItemClick = { pokemonModel, i ->
            when(i) {
                R.id.pokeright -> viewModel.updateRightPokemon(pokemonModel)
                R.id.pokeleft -> viewModel.updateLeftPokemon(pokemonModel)
            }
            dismiss()
            }
    }

}