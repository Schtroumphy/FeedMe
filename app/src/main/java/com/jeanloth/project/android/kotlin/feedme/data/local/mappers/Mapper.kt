package com.jeanloth.project.android.kotlin.feedme.data.local.mappers

interface Mapper<T, I> {

    fun from(entity : T) : I

    fun to(pojo: I) : T
}