package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BasketWrapperRepository

class UpdateBasketWrapperUseCase (
    private val repository: BasketWrapperRepository
) {
    operator fun invoke(wrappers: List<Wrapper<Basket>>?) = wrappers?.let { repository.update(wrappers) }
}