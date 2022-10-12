package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.ProductCategory
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItem
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.WrapperItemEntity

/**
 * Product entity to describe all products in baskets
 */
@Entity(tableName = "product")
class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var label: String = "",
    var image : String? = null,
    val unitPrice: Float = 0f,
    val category: Int = ProductCategory.FRUIT.code
) : WrapperItemEntity

// Command > (Basket > List<ProductWrapper> > Product) + List<ProductWrapper>