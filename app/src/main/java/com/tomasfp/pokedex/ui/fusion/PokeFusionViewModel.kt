package com.tomasfp.pokedex.ui.fusion

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokeFusionViewModel @Inject constructor(private val pokeRepository: HomeRepository) : ViewModel() {

    private val _selectedPokemon = MutableLiveData(SelectedPokemons())
    val selectedPokemon : LiveData<SelectedPokemons> = _selectedPokemon

    private val _state = MutableStateFlow<State>(State.Loading)
    val state: StateFlow<State> = _state


    fun getPokemons() {
        viewModelScope.launch {
            pokeRepository.getOriginalPokemons().collect { result ->
                result.onSuccess {
                    _state.value = State.PokemonList(it)
                }

            }
        }
    }


    sealed class State {
        object Loading : State()
        data class PokemonList(val pokeList: List<PokemonModel>) : State()
    }

    fun fuse(): String {
        return "https://images.alexonsager.net/pokemon/fused/${selectedPokemon.value?.leftPokemon?.getPokemonIndexNoPad()}/${selectedPokemon.value?.leftPokemon?.getPokemonIndexNoPad()}.${selectedPokemon.value?.rightPokemon?.getPokemonIndexNoPad()}.png"
    }

    fun updateLeftPokemon(pokemonModel: PokemonModel) {
        _selectedPokemon.postValue(_selectedPokemon.value?.copy(leftPokemon = pokemonModel) )
    }

    fun updateRightPokemon(pokemonModel: PokemonModel) {
        _selectedPokemon.postValue(_selectedPokemon.value?.copy(rightPokemon = pokemonModel) )
    }

    data class SelectedPokemons(
        var leftPokemon : PokemonModel? = null,
        var rightPokemon: PokemonModel? = null
    )
}