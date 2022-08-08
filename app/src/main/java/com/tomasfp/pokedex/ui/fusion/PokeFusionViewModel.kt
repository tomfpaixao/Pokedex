package com.tomasfp.pokedex.ui.fusion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tomasfp.pokedex.model.PokemonModel
import com.tomasfp.pokedex.model.extensions.getPokemonIndexNoPad
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

    private val _nameFusioned = MutableStateFlow("")
    val nameFusioned: StateFlow<String> = _nameFusioned


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

    fun fuse(): Flow<String> = flow {
        val leftPokemonIndex = selectedPokemon.value?.leftPokemon?.getPokemonIndexNoPad()
        val rightPokemonIndex = selectedPokemon.value?.rightPokemon?.getPokemonIndexNoPad()
        emit("https://images.alexonsager.net/pokemon/fused/${leftPokemonIndex}/${leftPokemonIndex}.${rightPokemonIndex}.png")

        fuseNames(selectedPokemon.value?.leftPokemon?.name,  selectedPokemon.value?.rightPokemon?.name)
    }

    private fun fuseNames(nameLeft: String?, nameRight: String?) {
        val prefixLeft = nameLeft?.let {
            it.take(it.length / 2)
        }

        val suffix = nameRight?.let {
            it.takeLast(it.length / 2)
        }
        _nameFusioned.value = "You got a $prefixLeft$suffix."
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