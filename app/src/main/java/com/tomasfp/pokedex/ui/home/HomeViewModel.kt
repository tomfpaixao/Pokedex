package com.tomasfp.pokedex.ui.home

import android.text.Editable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    private val _searchState = MutableStateFlow<State>(State.Loading)
    val searchState: StateFlow<State> = _searchState



    fun getPokemonsPaged() {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.pokemons.cachedIn(viewModelScope)
                .collectLatest {
                    _state.value = State.PokemonList(it)
                }
        }
    }

    fun searchPokemon(text: Editable?) {
        viewModelScope.launch {
            repository.searchPokemon(text.toString()).collectLatest { result ->
                result.onSuccess {
                    _searchState.value = SearchState.PokemonList(it)
                }

            }
        }
    }

    sealed class State {
        object Loading : State()
        data class PokemonList(val pokeList: PagingData<PokemonModel>) : State()
    }

    sealed class SearchState {
        object Loading : State()
        data class PokemonList(val pokeList: List<PokemonModel>) : State()
    }

}