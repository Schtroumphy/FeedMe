package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveWrapperUseCase
import java.time.LocalDate
import javax.inject.Inject

class SaveCommandUseCase @Inject constructor(
    private val repository: CommandRepository,
    private val saveBasketWrapperUseCase: SaveBasketWrapperUseCase,
    private val saveProductWrapperUseCase: SaveProductWrapperUseCase,
) {
    val TAG = "SaveCommandUseCase"

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

       Log.d(TAG, "Command to save : ${command}")
        var commandId = repository.save(command)
        Log.d(TAG, "Command id : ${commandId}")

        // Save basket wrappers
        val basketWrappers = command.basketWrappers.onEach {
            it.parentId = commandId
        }
        Log.d(TAG, "Saving basket wrappers : $basketWrappers")
        val basketWrappersIds = saveBasketWrapperUseCase(basketWrappers)

        // Save product wrappers
        val productWrappers = command.productWrappers.onEach {
            it.parentId = commandId
        }
        Log.d(TAG, "Saving basket wrappers : $basketWrappers")
        val productWrappersIds = saveProductWrapperUseCase(productWrappers)

        return commandId != 0L && basketWrappersIds.size == basketWrappers.size && productWrappersIds.size == productWrappers.size
    }
}