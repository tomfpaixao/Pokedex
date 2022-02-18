package com.tomasfp.pokedex.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tomasfp.pokedex.model.PokemonDetailResponse
import com.tomasfp.pokedex.model.PokemonModel
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

    private val _event = Channel<Event>()
    val event: Flow<Event> = _event.receiveAsFlow()

    fun getPokemonDetail(name: String) {
        viewModelScope.launch {
            _state.value = State.Loading
            repository.getPokemonDetail(name).collectLatest { result ->
                result.onSuccess {
                    _state.value = State.PokemonDetail(it)
                }
                result.onFailure {
                    _event.send(Event.Error)
                }
            }
        }
    }

    sealed class State {
        object Loading : State()
        data class PokemonDetail(val detail: PokemonDetailResponse) : State()
    }

    sealed class Event {
        object Error : Event()
    }

}