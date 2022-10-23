package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.core.extensions.updateWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper.Companion.toWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.ObserveAllBasketsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommandVM @Inject constructor(
    private val observeAllBasketsUseCase: ObserveAllBasketsUseCase
) : ViewModel() {

    private val _baskets : MutableStateFlow<List<Basket>> = MutableStateFlow(emptyList())
    val baskets : StateFlow<List<Basket>> = _baskets.asStateFlow()

    private val _basketWrappers : MutableStateFlow<List<Wrapper<Basket>>?> = MutableStateFlow(emptyList())
    val basketWrappers = _basketWrappers.asStateFlow()

    var client : AppClient? = null
    private set

    init {
        viewModelScope.launch {
            observeAllBasketsUseCase.invoke().collect {
                _baskets.value = it
                _basketWrappers.emit(it.map { it.toWrapper() })
            }
        }

        viewModelScope.launch {
            _basketWrappers.collect{
                Log.d("CommandVM", "Basket wrappers received : $it")
            }
        }
    }

    fun canAskUserToSaveCommand() : Boolean = client != null && _basketWrappers.value?.any { it.quantity > 0 } == true

    fun setClient(client : AppClient){
        this.client = client
        Log.d("CommandVM", "Client selected : ${this.client}")
    }

    fun setBasketQuantityChange(basketId: Long, quantity : Int){
        val basket = _baskets.value.firstOrNull { it.id == basketId }
        basket?.let {
            viewModelScope.launch {
                _basketWrappers.emit(null)
                _basketWrappers.emit(_basketWrappers.value?.toMutableList()?.updateWrapper(basket, quantity) ?: mutableListOf<Wrapper<Basket>>().updateWrapper(basket, quantity))
                Log.d("CommandVM", "Basket wrappers : ${_basketWrappers.value}")
            }
        }
    }

    fun clearCurrentCommand(){
        viewModelScope.launch {
            client = null
            _basketWrappers.emit(_baskets.value.map { it.toWrapper() })
        }
    }

    fun saveCommand(client : AppClient?= null) {

    }
}