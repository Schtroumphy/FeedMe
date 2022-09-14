package com.jeanloth.project.android.kotlin.feedme.features.command.data.repositoryImpl

import android.util.Log
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.dao.ProductDao
import com.jeanloth.project.android.kotlin.feedme.features.command.data.mappers.ProductEntityMapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.repository.ProductRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val dao : ProductDao,
    private val mapper : ProductEntityMapper,
    private val moshi: Moshi
) : ProductRepository {

    override fun save(product: Product) {
        dao.insert(mapper.to(product))
    }

    override fun observeProducts(): Flow<List<Product>> {
        return dao.observeAll().map { products -> products.map { mapper.from(it) } }
    }

    override fun remove(product: Product) {
        return dao.delete(mapper.to(product))
    }

    override fun syncProduct() {
        val json = javaClass.getResource("/products.json")?.readText()
        Log.d("ProductRepositoryImpl", "JSON : $json")
        val dataList = Types.newParameterizedType(List::class.java, Product::class.java)
        val adapter: JsonAdapter<List<Product>> = moshi.adapter(dataList)

        val result = adapter.fromJson(json) ?: emptyList()
        Log.d("ProductRepositoryImpl", "Products : $result")
        return dao.insertAll(result.map{ mapper.to(it) })
    }
}
