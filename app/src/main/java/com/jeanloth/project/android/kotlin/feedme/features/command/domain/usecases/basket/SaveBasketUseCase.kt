package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import javax.inject.Inject

class SaveBasketUseCase @Inject constructor(
    private val repository: BasketRepository,
    private val saveProductWrapperUseCase: SaveProductWrapperUseCase
) {
    operator fun invoke(basket: Basket) : Boolean {
        Log.d("SaveBasketUseCase", "Basket to save : ${basket}")
        var basketId = repository.save(basket)
        Log.d("SaveBasketUseCase", "Basket id : ${basketId}")

        val wrappers = basket.wrappers.onEach {
            it.parentId = basketId
        }
        Log.d("SaveProductWrapperUseCase", "Saving product wrappers : $wrappers")
        val wrappersIds = saveProductWrapperUseCase(wrappers)

        return basketId != 0L
    }
}