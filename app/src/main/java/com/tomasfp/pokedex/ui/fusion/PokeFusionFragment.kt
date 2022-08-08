package com.tomasfp.pokedex.ui.fusion

import android.app.Dialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.FragmentDetailLayoutBinding
import com.tomasfp.pokedex.databinding.FragmentPokefusionLayoutBinding
import com.tomasfp.pokedex.ui.home.HomeViewModel
import com.tomasfp.pokedex.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokeFusionFragment : Fragment(R.layout.fragment_pokefusion_layout) {

    private val binding by viewBinding<FragmentPokefusionLayoutBinding>()

    private val viewModel by activityViewModels<PokeFusionViewModel>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pokeleft.setOnClickListener { v -> showPokemonSelector(v) }
        binding.pokeright.setOnClickListener {  v -> showPokemonSelector(v) }

        binding.fusionButton.setOnClickListener { fusePokemons() }

        binding.fusionedImage.load(R.drawable.ic_pokeball)

        setObservers()

    }

    private fun setObservers() {
            viewModel.selectedPokemon.observe(viewLifecycleOwner) {
                binding.pokeright.text = it.rightPokemon?.name ?: "POKEMON"
                binding.pokeleft.text = it.leftPokemon?.name ?: "POKEMON"

                if(it.leftPokemon != null && it.rightPokemon != null)
                    binding.fusionButton.isEnabled = true
            }

        lifecycleScope.launch {
            viewModel.nameFusioned.collectLatest {
                binding.pokemonNameFusioned.text = it
            }
        }
    }

    private fun fusePokemons() {
        lifecycleScope.launch {
            viewModel.fuse().collectLatest {
                binding.fusionedImage.load(it) { placeholder(R.drawable.ic_pokeball) }
            }
        }
    }


    private fun showPokemonSelector(v: View) {
        val dialog = PokemonSelectorBottomSheet(v.id)
        dialog.show(parentFragmentManager,javaClass.canonicalName)
    }


}