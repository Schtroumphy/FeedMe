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
import com.jeanloth.project.android.kotlin.feedme.core.extensions.updateWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper.Companion.toWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.ObserveAllBasketsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.ObserveAllProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommandVM @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val observeAllBasketsUseCase: ObserveAllBasketsUseCase,
    private val observeAllProductsUseCase: ObserveAllProductsUseCase
) : ViewModel() {

    private val _baskets : MutableStateFlow<List<Basket>> = MutableStateFlow(emptyList())
    val baskets : StateFlow<List<Basket>> = _baskets.asStateFlow()

    private val _products : MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products : StateFlow<List<Product>> = _products.asStateFlow()

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
    var commandPrice = 0

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
    }

    fun canAskUserToSaveCommand() : Boolean = client.value != null && _basketWrappers.value.any { it.quantity > 0 }

    fun updateClient(client : AppClient?){
        Log.d("CommandVM", "Client selected : ${this.client}")
        withMutableSnapshot {
            _client = client
        }
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
     * Update quantity map
     * Mpa of product / quantity
     * TODO Replace by product wrapper ?
     */
    fun updateProductQuantityMap(productQuantityMap: MutableMap<Product, Int?>, product: Product, quantity: Int?) : MutableMap<Product, Int?>{
        return productQuantityMap.apply {
            this[product] = quantity
        }
    }

    /**
     * Clear client, basket wrappers and product quantity map
     */
    fun clearCurrentCommand(){
        viewModelScope.launch {
            updateClient(null)
            _basketWrappers.emit(baskets.value.map { it.toWrapper() })
            _productWrappers.emit(products.value.map { it.toWrapper() })
        }
    }

    fun saveCommand() {
        // TODO : Save command method


    }
}