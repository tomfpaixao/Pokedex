package com.tomasfp.pokedex.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.PokemonResponse
import com.tomasfp.pokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state

    fun getPokemonList() =
        repository.getPokemonList()
            .onStart { _state.value = State.Loading }
            .onEach { result ->
                result.onSuccess {  pokemonList ->
                    _state.value = State.Success(pokemonList.results)
                }
                    .onFailure {
                        _state.value = State.Error(it.message)
                    }
            }
            .onCompletion { _state.value = State.Idle }
            .catch { _state.value = State.Error() }
            .launchIn(viewModelScope)



    sealed class State {
        data class Success(val pokemonList : List<PokemonModel>) : State()
        data class Error(val error : String? = "") : State()
        object Loading : State()
        object Idle : State()
    }

}