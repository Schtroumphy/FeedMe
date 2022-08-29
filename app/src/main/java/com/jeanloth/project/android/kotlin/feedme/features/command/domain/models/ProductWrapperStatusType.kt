package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models

enum class ProductWrapperStatusType(val code : Int) {
    TO_DO(1),
    IN_PROGRESS(2),
    DONE(3),
    CANCELED(4);

    companion object {
        fun getProductWrapperStatusFromCode(code : Int) : ProductWrapperStatusType = when(code){
            TO_DO.code -> TO_DO
            IN_PROGRESS.code -> IN_PROGRESS
            DONE.code -> DONE
            CANCELED.code -> CANCELED
            else -> TO_DO
        }
    }

}