package com.tomasfp.pokedex.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tomasfp.pokedex.R
import com.tomasfp.pokedex.databinding.PokemonLoadingStateLayoutBinding
import com.tomasfp.pokedex.utils.gone
import com.tomasfp.pokedex.utils.visible

class PokemonLoadingStateAdapter(private val retryPressedListener: RetryPressedListener) :
    LoadStateAdapter<PokemonLoadingStateAdapter.LoadingStateItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateItemViewHolder = LoadingStateItemViewHolder(
        PokemonLoadingStateLayoutBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pokemon_loading_state_layout, parent, false
            )
        ), retryPressedListener
    )

    override fun onBindViewHolder(holder: LoadingStateItemViewHolder, loadState: LoadState) = holder.bind(loadState)

    class LoadingStateItemViewHolder(
        private val binding: PokemonLoadingStateLayoutBinding,
        private val listener : RetryPressedListener
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { listener.onRetryPressed() }
        }

        fun bind(loadState: LoadState) {
            with(binding){
                when(loadState) {
                    is LoadState.NotLoading -> progressBar.gone()
                    LoadState.Loading -> progressBar.visible()
                    is LoadState.Error -> {
                        progressBar.gone()
                        retryButton.visible()
                        error.apply {
                            visible()
                            text = error
                        }
                    }
                }
            }
        }
    }


    fun interface RetryPressedListener {
        fun onRetryPressed()
    }
}