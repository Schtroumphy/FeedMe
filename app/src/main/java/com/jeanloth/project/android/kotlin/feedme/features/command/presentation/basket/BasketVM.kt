package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.basket

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.BuildConfig
import com.jeanloth.project.android.kotlin.feedme.core.AppPreferences
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.ObserveAllBasketsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.ObserveAllProductsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.SaveProductUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.products.SyncProductUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketVM @Inject constructor(
    private val observeAllBasketsUseCase: ObserveAllBasketsUseCase,
    private val saveBasketUseCase: SaveBasketUseCase,
) : ViewModel() {

    private val _baskets : MutableStateFlow<List<Basket>> = MutableStateFlow(emptyList())
    val baskets : StateFlow<List<Basket>> = _baskets.asStateFlow()

    private val basketProductWrappers : MutableList<Wrapper<Product>> = mutableListOf()

    init {
        viewModelScope.launch {
            observeAllBasketsUseCase.invoke().collect {
                _baskets.value = it
            }
        }
    }

    suspend fun saveBasket(label: String, price: Int, productQuantity: Map<Product, Int?>) : Boolean{
        var result = false
        val wrapperList = mutableListOf<Wrapper<Product>>().apply {
            productQuantity.forEach {
                it.value?.let { quantity ->
                    this.add(
                        Wrapper(item = it.key, quantity = quantity)
                    )
                }
            }
        }

        val basket = Basket(
            label = label,
            price = price.toFloat(),
            wrappers = wrapperList
        )
        Log.d("ProductVM", "Basket to save : $basket")
        val job = viewModelScope.launch(Dispatchers.IO){
            result = saveBasketUseCase(basket)
        }
        job.join()
        return  result
    }

    fun removebasket(basket: Basket){
        viewModelScope.launch(Dispatchers.IO){
            //removeBasketUseCase.invoke(basket)
        }
    }
}