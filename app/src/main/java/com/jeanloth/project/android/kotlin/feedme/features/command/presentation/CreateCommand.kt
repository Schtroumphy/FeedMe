package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.DeliveryDateSpinner
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.GetIntValueDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.PricesRow
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client.GetStringValueDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.ProductItem
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.RoundedProductItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun AddCommandPage(
    selectedClient: AppClient? = null,
    clients: List<AppClient> = emptyList(),
    basketItems: List<BasketItem> = emptyList(),
    basketWrappers: List<Wrapper<Basket>> = emptyList(),
    productWrappers: List<Wrapper<Product>> = emptyList(),
    onNewClientAdded: ((String) -> Unit)? = null,
    onClientSelected: ((AppClient) -> Unit)? = null,
    onBasketQuantityChange: ((Long, Int) -> Unit)? = null, // Transfer basket id and new quantity to viewModel
    onProductQuantityChange: ((Long, Int) -> Unit)? = null, // Transfer product id and new quantity to viewModel
){
    val maxStepCount = 3
    var currentStep by remember { mutableStateOf(1) }
    var clientSelected by remember { mutableStateOf(selectedClient)}

    val nextStepEnabled = when(currentStep){
        1 -> clientSelected != null && basketWrappers.any { it.quantity > 0 }
        2 -> clientSelected != null && productWrappers.any { it.quantity > 0 }
        else -> true
    }

    // Price states // TODO Integrate dialog in price row composable ?
    var selectedPrice by remember { mutableStateOf(0) }
    var customQuantity by remember { mutableStateOf(-1) }
    val quantities = listOf(10, 15, 20, 25, customQuantity)

    // Choose custom basket price dialog
    val showCustomDialogWithResult = rememberSaveable { mutableStateOf(false) }

    if (showCustomDialogWithResult.value) {
        GetIntValueDialog {
            showCustomDialogWithResult.value = false
            customQuantity = it
            selectedPrice = it
        }
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (clientSpinner, content, nextPreviousButtons) = createRefs()

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(clientSpinner) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ){
            // Choose client to associate to command
            ClientSpinner(
                isEnabled = currentStep != maxStepCount,
                elements = clients,
                client = clientSelected,
                onNewElementAdded = {
                    onNewClientAdded?.invoke(it)
                },
                clientSelected = {
                    clientSelected = it
                    onClientSelected?.invoke(it)
                }
            )

            DeliveryDateSpinner(
                isEnabled = currentStep != maxStepCount
            )
        }

        /**
         * Step 1 : Choose presaved basket if exist // TODO : Go to step 2 directly if there is no presaved baskets
         * Step 2 : Choose product individually to add to basket
         * Step 3 : // TODO : Display a brief of command and define price
         */
        when(currentStep){
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
                    items(basketWrappers) {
                        BasketItem(
                            basketWrapper = it,
                            editMode = true,
                            onBasketQuantityChange = { basketIdQuantity ->
                                onBasketQuantityChange?.invoke(basketIdQuantity.first, basketIdQuantity.second)
                            }
                        )
                    }
                }
            }
            2 -> {
                // Products list
                LazyVerticalGrid(
                    cells = GridCells.Fixed(2),
                    modifier = Modifier.constrainAs(content){
                        top.linkTo(clientSpinner.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(nextPreviousButtons.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ){
                    items(productWrappers){
                        it.let {
                            ProductItem(
                                product = it.item,
                                quantity = it.quantity,
                                modifier = Modifier,
                                onQuantityChange = { quantity ->
                                    Log.d("Create Basket", "Quantity received : $quantity")
                                    onProductQuantityChange?.invoke(it.item.id, quantity ?: 0)
                                }
                            )
                        }
                    }

                    item {
                        AddProductButton()
                    }
                }
            }
            3 -> {
                LazyColumn(
                    modifier = Modifier.constrainAs(content){
                        top.linkTo(clientSpinner.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(nextPreviousButtons.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                ){
                    // Prices row selection
                    item {
                        Box(Modifier.padding(top = 10.dp)) {
                            PricesRow(Modifier.fillMaxSize().align(Center), quantities, selectedPrice){ price ->
                                if (price == customQuantity) showCustomDialogWithResult.value = true
                                selectedPrice = price
                            }
                        }
                    }

                    // Baskets encart
                    items(basketWrappers.filter { it.quantity > 0 }) {
                        /*Box(
                            Modifier
                                .padding(top = 15.dp)
                                .border(
                                    width = 1.5.dp,
                                    color = Orange1,
                                    shape = RoundedCornerShape(20.dp)
                                )
                        ){
                            basketWrappers.filter { it.quantity > 0 }.forEach {*/
                                BasketItem(
                                    basketWrapper = it,
                                    editMode = false
                                )
                           // }
                       // }
                    }

                    // Individual products
                    item {
                        Text("Produits individuels", modifier = Modifier.padding(vertical = 12.dp, horizontal = 5.dp))
                    }

                    productWrappers.filter { (it.quantity ?: 0) > 0 }.chunked(3).forEach {
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(top = 10.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ){
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

        // Floating action buttons (next and previous)
        Row(
            Modifier
                .fillMaxWidth()
                .constrainAs(nextPreviousButtons) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            // TODO : Refactor by creating a single button composable with paramters for previous/next state
            // Previous button
            FloatingActionButton(
                onClick = {
                    if(currentStep > 0) currentStep -= 1 // Change to previous step if possible - Only when step > 1
                },
                containerColor = Purple80,
                modifier = Modifier
                    .scale(0.6f)
                    .alpha(if (currentStep > 1) 1f else 0f), // Do not display in step 1
            ) {
                Icon(imageVector = Icons.Filled.ArrowBackIos, contentDescription = "")
            }

            // Next button
            FloatingActionButton(
                onClick = {
                    // Change to next step if possible
                    Log.d("CreateCommand", "Step $currentStep")
                    if(nextStepEnabled && currentStep < maxStepCount) currentStep += 1
                },
                containerColor = if(nextStepEnabled) {
                    if(currentStep == maxStepCount) Orange1 else Purple80
                } else Gray1,
                modifier = Modifier.scale(0.6f),
            ) {
                Icon(imageVector = if(currentStep != maxStepCount) Icons.Filled.ArrowForwardIos else Icons.Filled.Check, contentDescription = "", tint = if(currentStep == maxStepCount) White else Black)
            }
        }
    }
}

@Composable
@Preview
fun AddQuantityBox(
    modifier: Modifier = Modifier,
    quantity : Int = 0,
    onQuantityChange : ((Int)-> Unit)? = null
){
    val interactionSource = remember { MutableInteractionSource() }
    val rippleColor = Color.Red

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically,
            modifier = modifier
                .clip(RoundedCornerShape(5.dp))
                .background(if (quantity > 0) Vert0 else Gray1),
        ){
            Icon(imageVector = Icons.Filled.Remove, contentDescription = "", modifier = Modifier
                .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                .clickable(enabled = quantity > 0) {
                    if (quantity > 0) {
                        val quantityToSend = quantity - 1
                        Log.d("AddQuantity", "Quantity - : $quantity")
                        onQuantityChange?.invoke(quantityToSend)
                    }
                }
                .padding(10.dp)
                .size(10.dp)
                .alpha(if (quantity > 0) 1f else 0f)
                .align(CenterVertically)
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = rippleColor)
                )

            )
            Text(text = quantity.toString(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
            Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .clickable {
                    val quantityToSend = quantity + 1
                    Log.d("AddQuantity", "Quantity + : $quantity")
                    onQuantityChange?.invoke(quantityToSend)
                }
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = Green)
                )
                .padding(10.dp)
                .size(10.dp)
                .align(CenterVertically)
            )

        }
}


@Composable
fun ClientSpinner(
    modifier : Modifier = Modifier,
    client : AppClient?= null,
    isEnabled : Boolean = true,
    widthPercentage : Float = 0.6f,
    @StringRes labelId : Int = R.string.client,
    elements : List<AppClient> = emptyList(),
    onNewElementAdded : ((String) -> Unit)? = null,
    clientSelected : ((AppClient)-> Unit)? = null
){
    var selectedItem by remember { mutableStateOf<AppClient?>(client ?: AppClient(firstname = ""))}
    val selectionMode = remember { mutableStateOf(false)}
    val showCustomDialogWithResult = remember { mutableStateOf(false) }

    if(showCustomDialogWithResult.value){
        GetStringValueDialog (
            onNewClientAdded = {
                showCustomDialogWithResult.value = false
                onNewElementAdded?.invoke(it)
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
                ){
                    items(elements){
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
                            FloatingActionButton(onClick = {
                                showCustomDialogWithResult.value = true
                                selectedItem?.let {
                                    clientSelected?.invoke(it)
                                }
                            },modifier = Modifier
                                .align(BottomEnd)
                                .scale(0.5f)) {
                                Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                            }
                        }
                    }
                }
            }

            if(!selectionMode.value) {
                Text(selectedItem.toNameString(), style = MaterialTheme.typography.labelMedium)
            }
        }
        Text(stringResource(id = labelId), modifier = Modifier.align(Alignment.TopStart), style = MaterialTheme.typography.labelSmall)
    }
}
