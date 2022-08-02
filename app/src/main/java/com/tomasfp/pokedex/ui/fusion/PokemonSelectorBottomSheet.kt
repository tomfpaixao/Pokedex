package com.tomasfp.pokedex.ui.fusion

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.PokemonSelectorBottomSheetBinding
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonTypeModel
import com.tomasfp.pokedex.ui.home.adapter.PokemonPagedAdapter
import com.tomasfp.pokedex.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.flow.update
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
                    is PokeFusionViewModel.State.Loading -> {}
                    is PokeFusionViewModel.State.PokemonList -> {
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