package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.data

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import java.time.LocalDate

class CreateCommandParameters(
    val selectedClient: AppClient? = null,
    val clients: List<AppClient> = emptyList(),
    val basketWrappers: List<Wrapper<Basket>> = emptyList(),
    val productWrappers: List<Wrapper<Product>> = emptyList(),
)

class CreateCommandCallbacks(
    val onNewClientAdded: ((String) -> Unit)? = null,
    val onClientSelected: ((AppClient) -> Unit)? = null,
    val onBasketQuantityChange: ((Long, Int) -> Unit)? = null,
    val onProductQuantityChange: ((Long, Int) -> Unit)? = null,
    val onCommandPriceSelected: ((Int) -> Unit)? = null,
    val onCreateCommandClick: (() -> Unit)? = null,
    val onDateChanged: ((LocalDate) -> Unit)? = null,
)