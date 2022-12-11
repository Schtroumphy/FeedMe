package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AppButton
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.DeliveryDateSpinner
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.PricesRow
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client.GetStringValueDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.data.CreateCommandCallbacks
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.data.CreateCommandParameters
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.ProductItem
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.RoundedProductItem
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun AddCommandPage(
    parameters: CreateCommandParameters = CreateCommandParameters(),
    callbacks: CreateCommandCallbacks = CreateCommandCallbacks()
) {
    val maxStepCount = 3
    var currentStep by remember { mutableStateOf(if (parameters.basketWrappers.isEmpty()) 2 else 1) } // Go to step 2 directly if there is no baskets to display
    val displayPreviousButton = when (currentStep) {
        1 -> false
        2 -> !parameters.basketWrappers.isEmpty()
        else -> true
    }
    var clientSelected by remember { mutableStateOf(parameters.selectedClient) }
    var selectedPrice by remember { mutableStateOf<Int?>(null) }

    val nextStepEnabled = when (currentStep) {
        1 -> clientSelected != null
        2 -> parameters.productWrappers.any { it.quantity > 0 }
        3 -> selectedPrice != null
        else -> false
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (clientSpinner, content, nextPreviousButtons) = createRefs()

        // Header with client and delivery date selections
        ClientAndDeliveryDateRow(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(clientSpinner) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            clientSpinnerElements = parameters.clients,
            selectedClient = clientSelected,
            onNewClientAdded = { callbacks.onNewClientAdded?.invoke(it) },
            onClientSelected = {
                clientSelected = it
                callbacks.onClientSelected?.invoke(it)
            },
            onDateChanged = { callbacks.onDateChanged?.invoke(it) }
        )

        /**
         * Step 1 : Choose presaved basket if exist
         * Step 2 : Choose product individually to add to basket
         * Step 3 : Display a brief of command and define price
         */
        when (currentStep) {
            1 -> {
                // Baskets list
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(content) {
                            top.linkTo(clientSpinner.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(nextPreviousButtons.top)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                ) {
                    items(parameters.basketWrappers) {
                        BasketItem(
                            basketWrapper = it,
                            editMode = true,
                            onBasketQuantityChange = { basketIdQuantity ->
                                callbacks.onBasketQuantityChange?.invoke(
                                    basketIdQuantity.first,
                                    basketIdQuantity.second
                                )
                            }
                        )
                    }
                }
            }
            2 -> {
                // Products list
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    modifier = Modifier.constrainAs(content) {
                        top.linkTo(clientSpinner.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(nextPreviousButtons.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ) {
                    items(parameters.productWrappers) {
                        ProductItem(
                            product = it.item,
                            quantity = it.quantity,
                            modifier = Modifier,
                            onQuantityChange = { quantity ->
                                callbacks.onProductQuantityChange?.invoke(it.item.id, quantity ?: 0)
                            }
                        )
                    }

                    item { AddProductButton() }
                }
            }
            3 -> {
                LazyColumn(
                    modifier = Modifier.constrainAs(content) {
                        top.linkTo(clientSpinner.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(nextPreviousButtons.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ) {
                    // Prices row selection
                    item {
                        Box(Modifier.padding(top = 10.dp)) {
                            PricesRow(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .align(Center),
                                prices = listOf(10, 15, 20, 25)
                            ) { price ->
                                callbacks.onCommandPriceSelected?.invoke(price)
                                selectedPrice = price
                            }
                        }
                    }

                    // Baskets label
                    item {
                        Text(
                            "Paniers",
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 5.dp),
                            color = Orange1
                        )
                    }
                    if (parameters.basketWrappers.filter { it.quantity > 0 }.isEmpty()) item {
                        Text(
                            "Aucun panier sélectionné",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp, horizontal = 5.dp)
                        )
                    }

                    // Baskets encart
                    items(parameters.basketWrappers.filter { it.quantity > 0 }) {
                        BasketItem(
                            basketWrapper = it,
                            editMode = false
                        )
                    }

                    // Individual products label
                    item {
                        Text(
                            "Produits individuels",
                            modifier = Modifier.padding(vertical = 12.dp, horizontal = 5.dp),
                            color = Orange1
                        )
                    }

                    // Indeviduals product list items
                    parameters.productWrappers.filter { (it.quantity ?: 0) > 0 }.chunked(3)
                        .forEach {
                            item {
                                Row(
                                    modifier = Modifier
                                        .padding(top = 10.dp)
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    it.forEach { productWrapper ->
                                        RoundedProductItem(
                                            product = productWrapper.item,
                                            quantity = productWrapper.quantity
                                        )
                                    }
                                }
                            }
                        }
                }
            }
        }

        // Next/Previous action buttons
        Row(
            Modifier
                .fillMaxWidth()
                .constrainAs(nextPreviousButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {
            // Previous button
            AppButton(
                containerColor = Purple80,
                modifier = Modifier.alpha(if (displayPreviousButton) 1f else 0f),
                icon = Icons.Filled.ArrowBackIos,
                onClick = { if (currentStep > 0) currentStep -= 1 }
            )

            // TODO : Step points

            // Next button
            AppButton(
                containerColor = if (nextStepEnabled) {
                    if (currentStep == maxStepCount) Orange1 else Purple80
                } else Gray1,
                icon = if (currentStep != maxStepCount) Icons.Filled.ArrowForwardIos else Icons.Filled.Check,
                onClick = { if (nextStepEnabled && currentStep < maxStepCount) currentStep += 1 else callbacks.onCreateCommandClick?.invoke() },
            )
        }
    }
}

@Composable
fun ClientAndDeliveryDateRow(
    modifier: Modifier = Modifier,
    isClientSpinnerEnabled: Boolean = true,
    clientSpinnerElements: List<AppClient> = emptyList(),
    selectedClient: AppClient? = null,
    onNewClientAdded: ((String) -> Unit)? = null,
    onClientSelected: ((AppClient) -> Unit)? = null,
    onDateChanged: ((LocalDate) -> Unit)? = null,
    isDeliverDateEnabled: Boolean = true,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = CenterVertically
    ) {
        // Choose client
        ClientSpinner(
            isEnabled = isClientSpinnerEnabled,
            elements = clientSpinnerElements,
            client = selectedClient,
            onNewClientAdded = { onNewClientAdded?.invoke(it) },
            clientSelected = { onClientSelected?.invoke(it) }
        )
        //Choose delivery date
        DeliveryDateSpinner(isEnabled = isDeliverDateEnabled, onDateSelected = {
            onDateChanged?.invoke(it)
        })
    }
}

@Composable
@Preview
fun AddQuantityBox(
    modifier: Modifier = Modifier,
    quantity: Int = 0,
    backgroundColor: Color = Gray1,
    onQuantityChange: ((Int) -> Unit)? = null
) {

    var quantityEdit by remember { mutableStateOf(quantity) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(if(quantityEdit > 0) Vert0 else Gray1),
    ) {
        Icon(
            imageVector = Icons.Filled.Remove,
            contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                .clickable(enabled = quantity > 0) {
                    if (quantity > 0) {
                        val quantityToSend = quantity - 1
                        quantityEdit = quantityToSend
                        onQuantityChange?.invoke(quantityToSend)
                    }
                }
                .padding(10.dp)
                .size(10.dp)
                .alpha(if (quantity > 0) 1f else 0f)
                .align(CenterVertically)
        )
        Text(
            text = quantity.toString(),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "",
            modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .clickable {
                    val quantityToSend = quantity + 1
                    quantityEdit = quantityToSend
                    onQuantityChange?.invoke(quantityToSend)
                }
                .padding(10.dp)
                .size(10.dp)
                .align(CenterVertically)
        )

    }
}

@Composable
fun ClientSpinner(
    modifier: Modifier = Modifier,
    client: AppClient? = null,
    isEnabled: Boolean = true,
    widthPercentage: Float = 0.6f,
    @StringRes labelId: Int = R.string.client,
    elements: List<AppClient> = emptyList(),
    onNewClientAdded: ((String) -> Unit)? = null,
    clientSelected: ((AppClient) -> Unit)? = null
) {
    var selectedItem by remember { mutableStateOf<AppClient?>(client ?: AppClient(firstname = "")) }
    val selectionMode = remember { mutableStateOf(false) }
    val showCustomDialogWithResult = remember { mutableStateOf(false) }

    if (showCustomDialogWithResult.value) {
        GetStringValueDialog(
            onNewClientAdded = {
                showCustomDialogWithResult.value = false
                onNewClientAdded?.invoke(it)
            }
        )
    }

    Box(modifier) {
        Box(
            Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(widthPercentage)
                .align(Center)
                //.height(35.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isEnabled) Jaune1 else Gray1)
                .clickable(enabled = isEnabled) {
                    selectionMode.value = true
                    selectedItem?.let { clientSelected?.invoke(it) }
                }
                .padding(10.dp)

        ) {
            AnimatedVisibility(visible = selectionMode.value) {
                LazyColumn(
                    modifier = Modifier.height(150.dp)
                ) {
                    items(elements) {
                        Column(
                            modifier = Modifier.clickable {
                                selectedItem = it
                                selectionMode.value = false
                                clientSelected?.invoke(it)
                            },
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(it.toNameString(), modifier = Modifier.padding(vertical = 10.dp))
                            Divider(color = Color.White)
                        }
                    }

                    // Add client floating action button
                    item {
                        Box(Modifier.fillMaxWidth()) {
                            FloatingActionButton(
                                onClick = {
                                    showCustomDialogWithResult.value = true
                                    selectedItem?.let {
                                        clientSelected?.invoke(it)
                                    }
                                }, modifier = Modifier
                                    .align(BottomEnd)
                                    .scale(0.5f)
                            ) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                            }
                        }
                    }
                }
            }

            if (!selectionMode.value) {
                Text(selectedItem.toNameString(), style = MaterialTheme.typography.labelMedium)
            }
        }
        Text(
            stringResource(id = labelId),
            modifier = Modifier.align(Alignment.TopStart),
            style = MaterialTheme.typography.labelSmall
        )
    }
}
