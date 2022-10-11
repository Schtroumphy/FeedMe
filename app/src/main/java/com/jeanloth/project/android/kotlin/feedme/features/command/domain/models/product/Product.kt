package com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product

import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.ProductCategory

data class Product(
    val id: Long = 0,
    val label:String,
    val image: String? = null,
    var imageId: Int = R.drawable.delicious_banana,
    val category: ProductCategory = ProductCategory.FRUIT
)