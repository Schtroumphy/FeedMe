package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.core.extensions.isAllProductsTreated
import com.jeanloth.project.android.kotlin.feedme.core.extensions.isTreated
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.UpdateCommandUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.googleApis.GetNominatimPredictionsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.CommandDetailIdArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommandDetailsVM @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val updateCommandUseCase: UpdateCommandUseCase,
    private val updateProductWrapperUseCase: UpdateProductWrapperUseCase,
    private val getNominatimPredictionsUseCase: GetNominatimPredictionsUseCase
) : ViewModel() {

    val TAG = javaClass.simpleName

    private val _currentCommand : MutableStateFlow<Command?> = MutableStateFlow(null)
    val currentCommand = _currentCommand.asSharedFlow()

    val predictions = mutableStateOf(listOf<CommandAddress>())

    // Get current command id from nav args
    val commandId = savedStateHandle.get<Long>(CommandDetailIdArgument) ?: 0L

    init {

        // Observe current command by id
        viewModelScope.launch(Dispatchers.IO) {
            observeCommandByIdUseCase(commandId).collect {
                val pws = it?.productWrappers
                val bws = it?.basketWrappers

                val isEquals = _currentCommand.value == it?.copy(
                    productWrappers = pws ?: emptyList(),
                    basketWrappers = bws ?: emptyList()
                )
                Log.d(TAG, "Is Equals : $isEquals")
                _currentCommand.emit(it?.copy(
                    productWrappers = pws ?: emptyList(),
                    basketWrappers = bws ?: emptyList()
                )) // Maybe do not emit because considering no changes occured
                Log.d("Details VM", "Observed : $it")
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _currentCommand.collect {
                Log.d(TAG, "Collected : $it")
            }
        }
    }

    /**
     * Update product/basket wrapper when quantity change
     */
    fun updateRealCommandQuantity(info : CommandQuantityInfo){

        Log.i(TAG, "Update real quantity : $info")

        viewModelScope.launch(Dispatchers.IO) {

            when(info.wrapperType) {
                WrapperType.COMMAND_INDIVIDUAL_PRODUCT -> {
                    _currentCommand.value?.productWrappers?.firstOrNull { it.id == info.wrapperId }?.realQuantity = info.newQuantity
                    updateProductWrapperUseCase(_currentCommand.value?.productWrappers)
                }
                WrapperType.COMMAND_BASKET_PRODUCT -> {
                    _currentCommand.value?.basketWrappers?.firstOrNull { it.id == info.basketId }?.item?.wrappers?.apply{
                        firstOrNull { it.id == info.wrapperId }?.realQuantity = info.newQuantity
                        updateProductWrapperUseCase(this)
                    }
                }

                else -> {}
            }

            // Update command status to in progress if possible
            changeStatusIfPredicatesOk(Status.IN_PROGRESS)
            changeStatusIfPredicatesOk(Status.DONE)
        }
    }

    private fun changeStatusIfPredicatesOk(newStatus : Status) {
        val command = _currentCommand.value ?: return

        val canChangeStatus = when(newStatus) {
            Status.TO_DO -> command.status == Status.IN_PROGRESS
            Status.IN_PROGRESS ->
                (command.status == Status.TO_DO || command.status == Status.DONE)
                        && command.productWrappers.isTreated()
                        || command.basketWrappers.isAllProductsTreated()
            Status.DONE ->
                (command.status == Status.TO_DO || command.status == Status.IN_PROGRESS)
                    && command.productWrappers.isTreated()
                    && command.basketWrappers.isAllProductsTreated()
            Status.DELIVERING -> command.status == Status.DONE
            Status.CANCELED -> command.status.order < Status.DELIVERING.order
            Status.PAYED -> command.status == Status.DELIVERING
        }
        viewModelScope.launch(Dispatchers.IO) {
            if(canChangeStatus) updateCommandUseCase(_currentCommand.value?.apply {
                status = newStatus
            })
        }
    }

    private fun fillQuantities() {
        _currentCommand.value?.apply {
            productWrappers.onEach {
                if(it.realQuantity < it.quantity) {
                    it.realQuantity = it.quantity
                }
            }
            updateProductWrapperUseCase(productWrappers)
            basketWrappers.onEach {
                it.item.wrappers.onEach {
                    if(it.realQuantity < it.quantity) {
                        it.realQuantity = it.quantity
                    }
                }
                updateProductWrapperUseCase(it.item.wrappers)

                // TODO Change basket wrapper quantity if product wrappers are complete
            }
        }
    }

    /**
     * Do something on command button action click
     * - Done : Complete all quantities in command and change status
     * - Deliver :
     */
    fun onDetailActionClick(action : CommandAction){
        viewModelScope.launch ( Dispatchers.IO ){
            when(action){
                CommandAction.DONE -> {
                    fillQuantities()
                    changeStatusIfPredicatesOk(Status.DONE)
                }
                CommandAction.DELIVER -> changeStatusIfPredicatesOk(Status.DELIVERING)
                CommandAction.PAY -> changeStatusIfPredicatesOk(Status.PAYED)
                CommandAction.CANCEL -> changeStatusIfPredicatesOk(Status.CANCELED)
            }
        }
    }

    fun getPredictions(input : String){
        viewModelScope.launch(Dispatchers.IO) {
            val liste = getNominatimPredictionsUseCase(input)
            Log.i("CommandDetailsVM", "Predictions : $liste")
            predictions.value = liste
        }
    }

    fun updateCommandAddress(address: CommandAddress) {
        // Clear predictions
        predictions.value = emptyList()

        _currentCommand.value?.deliveryAddress = address.description
        _currentCommand.value?.coordinates = address.coordinates

        viewModelScope.launch(Dispatchers.IO){
            updateCommandUseCase(_currentCommand.value)
        }
    }

    fun onCancelClick() {
        changeStatusIfPredicatesOk(Status.CANCELED)
    }

}