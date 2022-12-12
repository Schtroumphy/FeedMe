package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateProductWrapperUseCase
import javax.inject.Inject

class UpdateCommandUseCase @Inject constructor(
    private val repository: CommandRepository
) {
    val TAG = "SaveCommandUseCase"

    operator fun invoke(
        command: Command?
    ): Boolean {

        Log.d(TAG, "Command to save : $command")
        command ?: return false

        repository.update(command)
        return true
    }
}