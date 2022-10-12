package com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository


import kotlinx.coroutines.flow.Flow

interface BaseRepository<T> {

    fun save(item : T) : Long

    fun save(items : List<T>) : Array<Long>

    fun observeProducts() : Flow<List<T>>

    fun remove(item : T)

}