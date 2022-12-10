package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperType
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveCommandBasketUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveProductWrapperUseCase
import java.time.LocalDate
import javax.inject.Inject

class SaveCommandUseCase @Inject constructor(
    private val repository: CommandRepository,
    private val saveCommandBasketUseCase: SaveCommandBasketUseCase,
    private val saveBasketWrapperUseCase: SaveBasketWrapperUseCase,
    private val saveProductWrapperUseCase: SaveProductWrapperUseCase,
) {

    operator fun invoke(
        clientId : Long,
        deliveryDate : LocalDate,
        price : Int,
        basketWrappers : List<Wrapper<Basket>> = emptyList(),
        productWrappers : List<Wrapper<Product>> = emptyList()
    ) : Boolean {

        val command = Command(
            totalPrice = price,
            productWrappers = productWrappers,
            basketWrappers = basketWrappers,
            deliveryDate = deliveryDate,
            clientId = clientId
        )
        val commandId = repository.save(command)


        // Save basket wrappers
        val basketWrappers = command.basketWrappers.onEach {
            it.parentId = commandId
            it.wrapperType = WrapperType.COMMAND_BASKET
            it.item.basketId = saveCommandBasketUseCase(it.item) // Save command basket (as copy of current basket) and associated it to this wrapper
        }
        saveBasketWrapperUseCase(basketWrappers)

        // Save individuals product wrappers
        val productWrappers = command.productWrappers.onEach {
            it.parentId = commandId
            it.wrapperType = WrapperType.COMMAND_INDIVIDUAL_PRODUCT
        }
        saveProductWrapperUseCase(productWrappers)

        return commandId != 0L
    }
}