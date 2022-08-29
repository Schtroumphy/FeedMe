package com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.ProductCategory

/**
 * Product entity to describe all products in baskets
 */
@Entity(tableName = "product")
class ProductEntity(
    @PrimaryKey
    var id: Long = 0,
    var label: String = "",
    var image : String? = null,
    val category: Int = ProductCategory.FRUIT.code
)

// Command > (Basket > List<ProductWrapper> > Product) + List<ProductWrapper>