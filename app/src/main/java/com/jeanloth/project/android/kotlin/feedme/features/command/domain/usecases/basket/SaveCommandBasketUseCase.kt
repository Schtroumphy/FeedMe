package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketRepository
import javax.inject.Inject

/**
 * Command basket represent basket associated to command
 * It's a copy of current basket "type" to be able to update quantity without affect basket "type"
 */
class SaveCommandBasketUseCase @Inject constructor(
    private val repository: BasketRepository,
    private val saveProductWrapperUseCase: SaveProductWrapperUseCase
) {
    operator fun invoke(basket: Basket) : Long {
        val commandBasketId = repository.saveCommandBasket(basket)

        val wrappers = basket.wrappers.onEach {
            it.id = 0
            it.parentId = commandBasketId
            it.wrapperType = WrapperType.COMMAND_BASKET_PRODUCT
        }

        saveProductWrapperUseCase(wrappers)

        return commandBasketId
    }
}