package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeanloth.project.android.kotlin.feedme.core.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.feedme.core.extensions.updateWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper.Companion.toWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.ObserveAllBasketsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateBasketWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.ObserveAllProductsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandItemType
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.CommandQuantityInfo
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.CommandDetailIdArgument
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CommandDetailsVM @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val updateProductWrapperUseCase: UpdateProductWrapperUseCase,
) : ViewModel() {

    private val _currentCommand : MutableStateFlow<Command?> = MutableStateFlow(null)
    val currentCommand : StateFlow<Command?> = _currentCommand.asStateFlow()

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
        when(info.itemType){
            CommandItemType.INDIVIDUAL_PRODUCT -> {
                _currentCommand.value?.productWrappers?.firstOrNull { it.id == info.wrapperId }?.realQuantity = info.newQuantity
                Log.i("CommandVM", _currentCommand.value?.productWrappers?.map { "${it.realQuantity}" }?.joinToString(",") ?: "" )
                viewModelScope.launch(Dispatchers.IO) {
                    updateProductWrapperUseCase(_currentCommand.value?.productWrappers, true)
                }
            }
            CommandItemType.BASKET -> {
                _currentCommand.value?.basketWrappers?.firstOrNull { it.id == info.basketId }?.item?.wrappers?.firstOrNull { it.id == info.wrapperId }?.realQuantity = info.newQuantity
                viewModelScope.launch(Dispatchers.IO) {
                    _currentCommand.value?.basketWrappers?.firstOrNull { it.id == info.basketId }?.item?.wrappers?.let {
                        updateProductWrapperUseCase(_currentCommand.value?.productWrappers, isAssociatedToCommand = false)

                        // TO DO update realQuantity ok basketWrapper if all product wrappers realQuantity >= quantity
                        //updateBasketWrapperUseCase(_currentCommand.value?.basketWrappers)
                    }
                }
            }
        }
    }
}