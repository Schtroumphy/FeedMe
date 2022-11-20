package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketWrapperRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductWrapperRepository

class SaveBasketWrapperUseCase (
    private val repository: BasketWrapperRepository
) {
    operator fun invoke(wrappers: List<Wrapper<Basket>>) = repository.save(wrappers)
}