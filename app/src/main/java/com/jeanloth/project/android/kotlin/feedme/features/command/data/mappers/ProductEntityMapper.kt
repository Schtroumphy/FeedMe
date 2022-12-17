package com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers

import com.jeanloth.project.android.kotlin.feedme.core.interfaces.Mapper
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.simple.ProductEntity
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

class ProductEntityMapper : Mapper<ProductEntity, Product> {
    override fun from(entity: ProductEntity): Product {
        return Product(
            productId = entity.id,
            label = entity.label,
            image = entity.image,
            imagePath = entity.imagePath,
            category = entity.category
        )
    }

    override fun to(pojo: Product): ProductEntity {
        return ProductEntity(
            id = pojo.id,
            label = pojo.label,
            image = pojo.image,
            imagePath = pojo.imagePath,
            category = pojo.category
        )
    }
}