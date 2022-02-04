package com.tomasfp.pokedex.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonResponse
import com.tomasfp.pokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state


    fun getPokemonsPaged() {
        /*
        return repository.pokemons
            .map { pagingData ->
                pagingData.map {
                    it
                }
            }
            .cachedIn(viewModelScope)

         */
        viewModelScope.launch {
            _state.value = State.Loading
            repository.pokemons.cachedIn(viewModelScope)
                .collectLatest {
                    _state.value = State.PokemonList(it)
                }
        }
    }

    sealed class State {
        object Loading : State()
        data class PokemonList(val pokeList: PagingData<PokemonModel>) : State()
    }

}