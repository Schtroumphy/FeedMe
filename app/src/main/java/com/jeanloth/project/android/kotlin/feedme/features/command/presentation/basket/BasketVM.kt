package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.basket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.ObserveBasketsUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BasketVM @Inject constructor(
    private val observeBasketsUseCase: ObserveBasketsUseCase,
    private val saveBasketUseCase: SaveBasketUseCase,
) : ViewModel() {

    private val _baskets : MutableStateFlow<List<Basket>> = MutableStateFlow(emptyList())
    val baskets : StateFlow<List<Basket>> = _baskets.asStateFlow()

    private val basketProductWrappers : MutableList<Wrapper<Product>> = mutableListOf()

    init {
        viewModelScope.launch {
            observeBasketsUseCase.invoke().collect {
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
        val job = viewModelScope.launch(Dispatchers.IO){
            result = saveBasketUseCase(basket)
        }
        job.join()
        return  result
    }
}