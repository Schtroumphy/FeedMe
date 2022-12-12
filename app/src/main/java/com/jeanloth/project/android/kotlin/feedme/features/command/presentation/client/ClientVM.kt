package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.client.GetAllClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.client.RemoveClientUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.client.SaveClientUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClientVM @Inject constructor(
    private val saveClientUseCase: SaveClientUseCase,
    private val getAllClientsUseCase: GetAllClientUseCase,
    private val removeClientUseCase: RemoveClientUseCase,
) : ViewModel() {

    private val _allMSF : MutableStateFlow<List<AppClient>> = MutableStateFlow(emptyList())
    val allSF = _allMSF.asStateFlow()

    private val _clientUiState : MutableStateFlow<ClientUiState> = MutableStateFlow(ClientUiState.EmptyList)
    val clientUiState : StateFlow<ClientUiState> = _clientUiState.asStateFlow()

    init {
        viewModelScope.launch {
            getAllClientsUseCase.invoke().collect {
                _allMSF.value = it
                _clientUiState.value = if(it.isNullOrEmpty()){
                    ClientUiState.EmptyList
                } else {
                    ClientUiState.NoEmptyList(it)
                }
            }
        }
    }

    fun saveClient(clientName : String){
        viewModelScope.launch(Dispatchers.IO){
            val client = AppClient(firstname = clientName)
            saveClientUseCase.invoke(client)
        }
    }

    fun removeClient(client : AppClient){
        viewModelScope.launch(Dispatchers.IO){
            removeClientUseCase.invoke(client)
        }
    }
}