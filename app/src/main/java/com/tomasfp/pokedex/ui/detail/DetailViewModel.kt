package com.tomasfp.pokedex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state

    fun getPokemonDetail(name: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.getPokemonDetail(name).collectLatest { result ->
                result.onSuccess {
                    _state.value = State.PokemonDetail(it)
                }
                result.onFailure {
                    _state.value = State.Error(it.localizedMessage)
                }
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class Error(val message: String?) : State()
        data class PokemonDetail(val detail: PokemonDetailResponse) : State()
    }

    sealed class Event {
        object Error : Event()
    }

}