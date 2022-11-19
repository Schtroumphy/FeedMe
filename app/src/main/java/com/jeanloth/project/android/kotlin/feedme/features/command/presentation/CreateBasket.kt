package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.core.theme.BleuVert
import com.jeanloth.project.android.kotlin.feedme.core.theme.Purple80
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AddProductDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AppTextField
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.PricesRow
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.ProductItem

data class BasketItem(val product: Product? = null, val addButton: Boolean = false)

@Composable
@Preview
fun CreateBasketPage(
    basketItems: List<BasketItem> = emptyList(),
    onValidateBasket: ((String, Int, Map<Product, Int?>) -> Unit)? = null,
    onAddProduct: ((String, String?) -> Unit)? = null
) {
    // Basket info
    var label by remember { mutableStateOf("") }
    val productQuantity = remember { mutableStateMapOf<Product, Int?>() }

    // Price state
    var selectedPrice by remember { mutableStateOf(0) }

    // Validation state
    val validationEnabled = label.isNotEmpty() && selectedPrice != 0 && !productQuantity.isEmpty()

    // Add product data
    val showAddProductDialog = rememberSaveable { mutableStateOf(false) }
    if (showAddProductDialog.value) {
        AddProductDialog { name, uri ->
            name?.let {
                onAddProduct?.invoke(it, uri?.path)
            }
            showAddProductDialog.value = false
        }
    }

    // Page content
    Box(Modifier.fillMaxSize().padding(10.dp)) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = CenterHorizontally
        ) {

            // Basket label text field
            item {
                Box(Modifier.fillMaxWidth()) {
                    AppTextField(modifier = Modifier.align(Center), onValueChange = { label = it })
                }
            }

            // Prices row selection
            item {
                Box(Modifier.padding(top = 10.dp)) {
                    PricesRow(modifier = Modifier.fillMaxSize().align(Center), prices = listOf(10, 15, 20, 25)){ price ->
                        selectedPrice = price
                    }
                }
            }

            // Basket possible items and add product abutton
            basketItems.chunked(2).forEach {
                item {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        it.forEach { item ->
                            // TODO : Create custom list instead of adding null at the end for add button
                            if (item.product != null) {
                                ProductItem(
                                    modifier = Modifier.weight(1f),
                                    product = item.product,
                                    onQuantityChange = { quantity ->
                                        Log.d("Create Basket", "Quantity received : $quantity")
                                        if (quantity == null) productQuantity.remove(item.product) else productQuantity.put(
                                            item.product,
                                            quantity
                                        )
                                    },
                                    quantity = productQuantity[item.product] ?: 0
                                )
                            } else {
                                AddProductButton(
                                    modifier = Modifier.weight(1f),
                                    onAddProductClicked = {
                                        showAddProductDialog.value = true
                                    }
                                )
                            }
                        }
                    }
                }

            }
        }

        // Validation button
        FloatingActionButton(
            onClick = {
                if (validationEnabled) {
                    onValidateBasket?.invoke(label, selectedPrice, productQuantity)
                } else {
                    Log.d(
                        "CreateBasket",
                        "Not enough element - Label : $label, Price : $selectedPrice, map is empty: ${productQuantity.isEmpty()}"
                    )
                }
            },
            containerColor = if (validationEnabled) Purple80 else Color.LightGray,
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
        ) {
            Icon(Icons.Filled.Check, contentDescription = "")
        }
    }
}

@Composable
fun AddProductButton(modifier: Modifier = Modifier, onAddProductClicked: (() -> Unit)? = null) {
    Box(
        modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(110.dp)
            .clickable {
                onAddProductClicked?.invoke()
            },
        contentAlignment = Center
    ) {
        FloatingActionButton(
            onClick = {
                Log.d("CreateBasket", "Click to add product")
                onAddProductClicked?.invoke()
            },
            containerColor = BleuVert,
            modifier = Modifier
                .scale(0.8f)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "")
        }
    }
}
