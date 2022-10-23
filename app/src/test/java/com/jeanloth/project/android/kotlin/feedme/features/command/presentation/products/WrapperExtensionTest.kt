package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products

import com.google.common.truth.Truth.assertThat
import com.jeanloth.project.android.kotlin.feedme.core.extensions.toBasketDescription
import com.jeanloth.project.android.kotlin.feedme.core.extensions.updateProductWrapper
import com.jeanloth.project.android.kotlin.feedme.core.extensions.updateWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import org.junit.After
import org.junit.Before
import org.junit.Test

internal class WrapperExtensionTest {

    private var mutableWrapperList : MutableList<Wrapper<Product>> = mutableListOf()
    @Before
    fun setUp() {}

    @After
    fun after() {
        mutableWrapperList = mutableListOf()
    }

    /** Update product wrapper list **/
    @Test
    fun `updateProductWrapper() | add no existing wrapper | singleton With The Only Wrapper`() {
        val product = Product(label = "Orange")
        val quantity = 2

        val result = mutableWrapperList.updateWrapper(product, quantity)

        assertThat(result).isNotEmpty()
        assertThat(result.size).isEqualTo(1)
        assertThat(result).containsExactly(
            Wrapper(
                item = product,
                quantity = quantity
            )
        )
    }

    @Test
    fun `updateProductWrapper() | update existing wrapper | singleton With The Only Wrapper`() {
        val product = Product(label = "Orange")
        val quantity = 2
        val result = mutableWrapperList.updateWrapper(product, quantity)
        val result2 = result.updateProductWrapper(product, 3)

        assertThat(result2).isNotEmpty()
        assertThat(result2.size).isEqualTo(1)
        assertThat(result).containsExactly(
            Wrapper(
                item = product,
                quantity = 3
            )
        )
    }

    @Test
    fun `updateProductWrapper() | remove existing wrapper if quantity is zero | empty list`() {
        val product = Product(label = "Orange")
        val quantity = 2
        val result = mutableWrapperList.updateWrapper(product, quantity)
        val result2 = result.updateWrapper(product, 0)

        assertThat(result2).isEmpty()
    }

    @Test
    fun `updateProductWrapper() | WITHOUT removing existing wrapper if quantity is zero | singleton list`() {
        val product = Product(label = "Orange")
        val quantity = 2
        val result = mutableWrapperList.updateWrapper(product, quantity, false)
        val result2 = result.updateWrapper(product, 0, false)

        assertThat(result2.size).isEqualTo(1)
        assertThat(result2).isNotEmpty()
    }


    /** Basket descrption from wrapper<Product> list **/
    @Test
    fun `toBasketDescription | Empty wrapper list | No description`(){
        assertThat(mutableWrapperList).isEmpty()
        assertThat(mutableWrapperList.toBasketDescription()).isEmpty()
    }

    @Test
    fun `toBasketDescription | Singleton wrapper list | Single description with no comma`(){
        val product = Product(label = "Orange")
        val quantity = 2
        val newList = mutableWrapperList.updateWrapper(product, quantity)

        val description = newList.toBasketDescription()
        assertThat(newList).isNotEmpty()
        assertThat(newList.size).isEqualTo(1)
        assertThat(description).isNotEmpty()
        assertThat(description).isEqualTo("Orange x2")
        assertThat(description).doesNotContain(",")
    }

    @Test
    fun `toBasketDescription | Wrapper list with several items | Description with comma`(){
        val product = Product(label = "Orange")
        val quantity = 2
        var newList = mutableWrapperList.updateWrapper(product, quantity)

        val product2 = Product(label = "Pomme")
        val quantity2 = 3
        newList = newList.updateWrapper(product2, quantity2)

        val description = newList.toBasketDescription()

        assertThat(newList).isNotEmpty()
        assertThat(newList.size).isEqualTo(2)
        assertThat(description).isNotEmpty()
        assertThat(description).isEqualTo("Orange x2, Pomme x3")
        assertThat(description).contains(",")
    }
}