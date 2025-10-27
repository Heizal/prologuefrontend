package com.example.prologuefrontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prologuefrontend.data.model.AIPick
import com.example.prologuefrontend.data.repository.AIPickRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AIPickState{
    object Loading : AIPickState()
    data class Success(val data: AIPick) : AIPickState()
    data class Error(val message: String) : AIPickState()
}
class AIPickViewModel : ViewModel() {
    private val repository = AIPickRepository()

    private val _state = MutableStateFlow<AIPickState>(AIPickState.Loading)
    val state: StateFlow<AIPickState> = _state

    init{
        fetchAIPick()
    }

    private fun fetchAIPick(){
        viewModelScope.launch{
            try{
                val response = repository.getAIPick()
                _state.value = AIPickState.Success(response)
            } catch(e:Exception){
                _state.value = AIPickState.Error("Failed to fetch AI pick")
            }
        }
    }


}