package com.jeanloth.project.android.kotlin.feedme.core.interfaces

interface Mapper<T, I> {

    fun from(entity : T) : I

    fun to(pojo: I) : T
}