package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command.Companion.changeStatus
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.UpdateCommandUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandItemType
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandQuantityInfo
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.CommandDetailIdArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommandDetailsVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val updateCommandUseCase: UpdateCommandUseCase,
    private val updateProductWrapperUseCase: UpdateProductWrapperUseCase,
) : ViewModel() {

    val TAG = javaClass.simpleName

    private val _currentCommand : MutableStateFlow<Command?> = MutableStateFlow(null)
    val currentCommand = _currentCommand.asSharedFlow()

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
            }

            // Update command status to in progress if possible
            if(_currentCommand.value?.changeStatus(Status.DONE) == true){
                updateCommandUseCase(_currentCommand.value?.apply {
                    status = Status.DONE
                })
            } else if(_currentCommand.value?.changeStatus(Status.IN_PROGRESS) == true){
                updateCommandUseCase(_currentCommand.value?.apply {
                    status = Status.IN_PROGRESS
                })
            }
        }
    }
}