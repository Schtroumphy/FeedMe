package com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.command

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.CommandRepository
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveBasketWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.SaveProductWrapperUseCase
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.usecases.basket.UpdateProductWrapperUseCase
import javax.inject.Inject

class UpdateCommandUseCase @Inject constructor(
    private val repository: CommandRepository,
    private val saveBasketWrapperUseCase: SaveBasketWrapperUseCase,
    private val updateProductWrapperUseCase: UpdateProductWrapperUseCase,
) {
    val TAG = "SaveCommandUseCase"

    operator fun invoke(
        command: Command?
    ): Boolean {

        if(command == null) {
            Log.e(TAG, "Command to save is null")
            return false
        }

        Log.d(TAG, "Command to save : ${command}")
        //repository.update(command) // TODO To uncomment when user will be able to edit command info

        return true
    }
}