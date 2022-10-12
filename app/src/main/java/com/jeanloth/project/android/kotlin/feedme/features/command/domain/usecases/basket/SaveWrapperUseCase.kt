package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository

class SaveWrapperUseCase(
    private val repository: BaseRepository<Wrapper<Product>>
) {
    operator fun invoke(wrappers: List<Wrapper<Product>>) = repository.save(wrappers)
}