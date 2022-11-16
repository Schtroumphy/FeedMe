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

    val _baskets : MutableStateFlow<List<Basket>> = MutableStateFlow(emptyList())
    val baskets : StateFlow<List<Basket>> = _baskets.asStateFlow()

    val _products : MutableStateFlow<List<Product>> = MutableStateFlow(emptyList())
    val products : StateFlow<List<Product>> = _products.asStateFlow()

    private val _productsQuantityMap : MutableStateFlow<MutableMap<Product, Int?>> = MutableStateFlow(mutableMapOf())
    val productsQuantityMap = _productsQuantityMap.asStateFlow()

    private val _basketWrappers : MutableStateFlow<List<Wrapper<Basket>>> = MutableStateFlow(emptyList())
    val basketWrappers = _basketWrappers.asStateFlow()

    private var _client by mutableStateOf<AppClient?>(null)
    val client = snapshotFlow { _client }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null
    )

    init {
        viewModelScope.launch {
            observeAllBasketsUseCase().collect {
                _baskets.emit(it)
                _basketWrappers.emit(it.map { it.toWrapper() })
            }
        }
        viewModelScope.launch {
           // observeAllProductsUseCase().collect {
            //    _products.emit(it)
            //    prod
            // }
        }

        observeAllProductsUseCase().onEach {
            it.onEach {
                it.imageId = applicationContext.resources.getIdentifier(it.image, "drawable", applicationContext.packageName)
            }
                _products.emit(it)
                it.forEach { _productsQuantityMap.value[it] = null }
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
        basket?.let { test ->
            viewModelScope.launch {
                _basketWrappers.update {
                    it.toMutableList().updateWrapper(test, quantity, false)
                }

                Log.d("CommandVM", "Basket wrappers : ${basketWrappers.value}")
            }
        }
    }

    fun setProductQuantityChange(productId: Long, quantity : Int){
        val product = _products.value.firstOrNull { it.id == productId }
        product?.let { test ->
            viewModelScope.launch {
                _productsQuantityMap.update {
                    updateProductQuantityMap(it, product, quantity)
                }
                Log.d("CommandVM", "Product quantity map : ${_productsQuantityMap.value}")
            }
        }
    }

    fun updateProductQuantityMap(productQuantityMap: MutableMap<Product, Int?>, product: Product, quantity: Int?) : MutableMap<Product, Int?>{
        return productQuantityMap.apply {
            this[product] = quantity
        }
    }

    fun clearCurrentCommand(){
        viewModelScope.launch {
            updateClient(null)
            _basketWrappers.emit(baskets.value.map { it.toWrapper() })
        }
    }

    fun saveCommand(client : AppClient?= null) {
        // TODO : Save command method
    }
}