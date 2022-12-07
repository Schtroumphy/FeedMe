package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command.Companion.changeStatus
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.UpdateCommandUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandItemType
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandQuantityInfo
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.CommandDetailIdArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommandDetailsVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val updateCommandUseCase: UpdateCommandUseCase,
    private val updateProductWrapperUseCase: UpdateProductWrapperUseCase,
) : ViewModel() {

    private val _currentCommand : MutableStateFlow<Command?> = MutableStateFlow(null)
    val currentCommand : StateFlow<Command?> = _currentCommand.asStateFlow()

    // Get current command id from nav args
    val commandId = savedStateHandle.get<Long>(CommandDetailIdArgument) ?: 0L

    init {

        // Observe current command by id
        viewModelScope.launch(Dispatchers.IO) {
            observeCommandByIdUseCase(commandId).collect {
                _currentCommand.emit(it)
            }
        }
    }

    /**
     * Update product/basket wrapper when quantity change
     */
    fun updateRealCommandQuantity(info : CommandQuantityInfo){
        Log.i("CommandVM", "Update real quantity : $info")
        viewModelScope.launch(Dispatchers.IO) {

            when(info.itemType) {
                CommandItemType.INDIVIDUAL_PRODUCT -> {
                    _currentCommand.value?.productWrappers?.firstOrNull { it.id == info.wrapperId }?.realQuantity = info.newQuantity
                    Log.i("CommandVM", _currentCommand.value?.productWrappers?.map { "${it.realQuantity}" }?.joinToString(",") ?: "")
                    updateProductWrapperUseCase(_currentCommand.value?.productWrappers, true)
                }
                CommandItemType.BASKET -> {
                    _currentCommand.value?.basketWrappers?.firstOrNull { it.id == info.basketId }?.item?.wrappers?.firstOrNull { it.id == info.wrapperId }?.realQuantity = info.newQuantity
                    _currentCommand.value?.basketWrappers?.firstOrNull { it.id == info.basketId }?.item?.wrappers?.let {
                        updateProductWrapperUseCase(it, isAssociatedToCommand = false)
                    }
                }
            }

            // Update command status to in progress if possible
            if(_currentCommand.value?.changeStatus(Status.IN_PROGRESS) == true){
                updateCommandUseCase(_currentCommand.value?.apply {
                    status = Status.IN_PROGRESS
                })
            } else if(_currentCommand.value?.changeStatus(Status.DONE) == true){
                updateCommandUseCase(_currentCommand.value?.apply {
                    status = Status.DONE
                })
            }
        }
    }
}