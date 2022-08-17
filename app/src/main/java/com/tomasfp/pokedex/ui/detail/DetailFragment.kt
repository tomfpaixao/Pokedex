package com.tomasfp.pokedex.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.FragmentDetailLayoutBinding
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.model.extensions.capitalName
import com.tomasfp.pokedex.model.extensions.getBackgroundColor
import com.tomasfp.pokedex.model.extensions.getPokemonImage
import com.tomasfp.pokedex.model.extensions.mainType
import com.tomasfp.pokedex.utils.gone
import com.tomasfp.pokedex.utils.viewBinding
import com.tomasfp.pokedex.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment(R.layout.fragment_detail_layout) {

    private val binding by viewBinding<FragmentDetailLayoutBinding>()

    private val args: DetailFragmentArgs by navArgs()

    private val viewModel by viewModels<DetailViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPokemonDetail(args.pokemon.name)

        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    DetailViewModel.State.Loading -> { binding.loadingAnim.visible() }
                    is DetailViewModel.State.PokemonDetail ->
                    {
                        binding.loadingAnim.gone()
                        setDetails(it.detail)
                    }
                    is DetailViewModel.State.Error -> {}
                }
            }
        }

        setPokemonArgs()

    }

    private fun setPokemonArgs() {
        binding.apply {
            with(args.pokemon) {
                pokemonName.text = capitalName()
                textViewType.text = mainType().name
                //imageView.load(getPokemonImage()) { placeholder(R.drawable.ic_pokeball) }
            }
        }
    }

    private fun setDetails(detail: PokemonDetailResponse) {

        binding.root.apply {
            setBackgroundColor(args.pokemon.getBackgroundColor(context))
        }

        detail.stats.forEach {
            addStatBar(it.stat.name.take(3).uppercase(), it.base_stat)
        }

    }

    private fun addStatBar(statName: String, statValue: Int) {
        val v = context?.let { StatBarLayout(it) }
        v?.setBarValues(statName,statValue)
        binding.barLayout.addView(v)
    }
}