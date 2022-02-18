package com.tomasfp.pokedex.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.api.load
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.FragmentDetailLayoutBinding
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.utils.viewBinding
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
                    DetailViewModel.State.Loading -> {}
                    is DetailViewModel.State.PokemonDetail -> setDetails(it.detail)
                }
            }
        }

        binding.pokemonName.text = args.pokemon.name.replaceFirstChar { it.uppercase() }
        binding.textViewType.text = args.pokemon.type?.first().toString()
        binding.imageView.load(args.pokemon.getPokemonImage()) { placeholder(R.drawable.ic_pokeball)}

    }

    private fun setDetails(detail: PokemonDetailResponse) {
        binding.statBar1.label.text = detail.stats[0].stat.name.take(3).uppercase()
        binding.statBar1.value.text = detail.stats[0].base_stat.toString()
        binding.statBar1.bar.progress = detail.stats[0].base_stat

        binding.statBar2.label.text = detail.stats[1].stat.name.take(3).uppercase()
        binding.statBar2.value.text = detail.stats[1].base_stat.toString()
        binding.statBar2.bar.progress = detail.stats[1].base_stat

        binding.statBar3.label.text = detail.stats[2].stat.name.take(3).uppercase()
        binding.statBar3.value.text = detail.stats[2].base_stat.toString()
        binding.statBar3.bar.progress = detail.stats[2].base_stat


    }
}