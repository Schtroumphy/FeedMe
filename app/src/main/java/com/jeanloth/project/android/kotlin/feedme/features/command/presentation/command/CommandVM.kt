package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.Snapshot.Companion.withMutableSnapshot
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.core.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.feedme.core.extensions.updateWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper.Companion.toWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.ObserveAllBasketsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.GetCommandByIdUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.ObserveAllCommandsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.ObserveAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class CommandVM @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val observeAllBasketsUseCase: ObserveAllBasketsUseCase,
    private val observeAllProductsUseCase: ObserveAllProductsUseCase,
    private val observeAllCommandsUseCase: ObserveAllCommandsUseCase,
    private val getCommandByIdUseCase: GetCommandByIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase
) : ViewModel() {

    private val _baskets : MutableStateFlow<List<Basket>> = MutableStateFlow(emptyList())
    val baskets : StateFlow<List<Basket>> = _baskets.asStateFlow()

    private val _products : MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products : StateFlow<List<Product>> = _products.asStateFlow()

    private val _commands : MutableStateFlow<List<Command>> = MutableStateFlow(emptyList())
    val commands : StateFlow<List<Command>> = _commands.asStateFlow()

    private val _commandsByDate : MutableStateFlow<Map<String, List<Command>>> = MutableStateFlow(mutableMapOf())
    val commandsByDate : StateFlow<Map<String, List<Command>>> = _commandsByDate.asStateFlow()

    private val _productWrappers : MutableStateFlow<List<Wrapper<Product>>> = MutableStateFlow(emptyList())
    val productWrappers = _productWrappers.asStateFlow()

    private val _basketWrappers : MutableStateFlow<List<Wrapper<Basket>>> = MutableStateFlow(emptyList())
    val basketWrappers = _basketWrappers.asStateFlow()

    private var _client by mutableStateOf<AppClient?>(null)
    val client = snapshotFlow { _client }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    private val _currentCommand : MutableStateFlow<Command?> = MutableStateFlow(null)
    val currentCommand : StateFlow<Command?> = _currentCommand.asStateFlow()

    var selectedClient : AppClient? = null
    var commandPrice = 0
    var deliveryDate = LocalDate.now().plusDays(1)

    init {
        viewModelScope.launch {
            observeAllBasketsUseCase().collect {
                _baskets.emit(it)
                _basketWrappers.emit(it.map { it.toWrapper() })
            }
        }

        observeAllProductsUseCase().onEach {
            it.onEach {
                it.imageId = applicationContext.resources.getIdentifier(it.image, "drawable", applicationContext.packageName)
            }
            _products.emit(it)
            _productWrappers.emit(it.map { it.toWrapper()})
        }.launchIn(viewModelScope)

        viewModelScope.launch(Dispatchers.IO) {
            observeAllCommandsUseCase().collect {
                _commands.emit(it)
                _commandsByDate.emit(it.groupBy { it.deliveryDate }.toSortedMap().mapKeys { it.key.formatToShortDate() })
                Log.d("CommandVM", "Commands observed : $it")
            }
        }
    }

    fun canAskUserToSaveCommand() : Boolean = client.value != null && _basketWrappers.value.any { it.quantity > 0 }

    fun updateClient(client : AppClient?){
        Log.d("CommandVM", "Client selected : ${this.client}")
        withMutableSnapshot {
            _client = client
        }
        selectedClient = client
    }

    fun setBasketQuantityChange(basketId: Long, quantity : Int){
        val basket = _baskets.value.firstOrNull { it.id == basketId }
        basket?.let { basketNonNull ->
            viewModelScope.launch {
                _basketWrappers.update {
                    it.toMutableList().updateWrapper(basketNonNull, quantity, false)
                }
                Log.d("CommandVM", "Basket wrappers : ${basketWrappers.value}")
            }
        }
    }
    fun updateCommandPrice(price : Int){
        commandPrice = price
    }

    fun updateDeliveryDate(date : LocalDate){
        deliveryDate = date
    }

    fun setProductQuantityChange(productId: Long, quantity : Int){
        val product = _products.value.firstOrNull { it.id == productId }
        product?.let { productNonNull ->
            viewModelScope.launch {
                _productWrappers.update {
                    it.toMutableList().updateWrapper(productNonNull, quantity, false)
                }
                Log.d("CommandVM", "Product wrappers : ${_productWrappers.value}")
            }
        }
    }

    /**
     * Clear client, basket wrappers and product quantity map
     */
    fun clearCurrentCommand(){
        viewModelScope.launch {
            updateClient(null)
            deliveryDate = LocalDate.now().plusDays(1)
            commandPrice = 0
            _basketWrappers.emit(baskets.value.map { it.toWrapper() })
            _productWrappers.emit(products.value.map { it.toWrapper() })
        }
    }

    fun saveCommand() : Boolean {
        viewModelScope.launch(Dispatchers.IO) {
            saveCommandUseCase(
                clientId = selectedClient?.idClient ?: 0,
                deliveryDate = deliveryDate,
                price = commandPrice,
                basketWrappers = _basketWrappers.value.filter { it.quantity > 0 },
                productWrappers = _productWrappers.value.filter { it.quantity > 0 }
            )
        }
        clearCurrentCommand()
        return true
    }

    fun updateCurrentCommand(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentCommand.value = getCommandByIdUseCase(id)
        }
    }
}