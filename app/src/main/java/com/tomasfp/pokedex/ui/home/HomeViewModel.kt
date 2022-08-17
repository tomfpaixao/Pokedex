package com.tomasfp.pokedex.ui.home

import android.text.Editable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
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

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Loading)
    val searchState: StateFlow<SearchState> = _searchState


    val pokemons: Flow<PagingData<PokemonModel>> = repository.pokemons


    fun getPokemonsPaged() {
        viewModelScope.launch {
            _state.value = State.PokemonList(repository.pokemons.cachedIn(viewModelScope))
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
        data class PokemonList(val pokeList: Flow<PagingData<PokemonModel>>) : State()
    }

    sealed class SearchState {
        object Loading : SearchState()
        data class PokemonList(val pokeList: List<PokemonModel>) : SearchState()
    }

}