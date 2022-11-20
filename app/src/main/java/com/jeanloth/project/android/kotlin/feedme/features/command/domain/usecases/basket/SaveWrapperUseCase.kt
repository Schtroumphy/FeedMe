package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket

import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.BaseRepository

class SaveWrapperUseCase <T: WrapperItem>(
    private val repository: BaseRepository<Wrapper<out T>>
) {
    operator fun invoke(wrappers: List<Wrapper<T>>) = repository.save(wrappers)
}