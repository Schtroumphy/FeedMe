package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

/**
 * Wrapper type to be able to transfer the info to repositories
 */
enum class WrapperType {
    COMMAND_INDIVIDUAL_PRODUCT,
    COMMAND_BASKET_PRODUCT,
    BASKET_PRODUCT,

    COMMAND_BASKET,
    BASKET,
    NONE
}