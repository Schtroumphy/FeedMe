package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client.GetStringValueDialog

@Composable
@Preview
fun AddCommandPage(
    selectedClient : AppClient? = null,
    clients : List<AppClient> = emptyList(),
    basketWrappers : List<Wrapper<Basket>> = emptyList(),
    onNewClientAdded: ((String) -> Unit)? = null,
    onClientSelected: ((AppClient) -> Unit)? = null,
    onBasketQuantityChange: ((Long, Int) -> Unit)? = null // Transfer basket id and new quantity to viewModel
){

    var clientSelected by remember { mutableStateOf(selectedClient)}
    val firstNextStepEnabled = clientSelected != null && basketWrappers.any { it.quantity > 0 }
    Log.d("AddCommandPage", "Map basketId / Quantity : ${basketWrappers.map { (key, value) -> "[$key=$value]" }}")

    var currentStep by remember { mutableStateOf(1) }

    Log.d("AddCommandPage", "FirstNext step enabled : $firstNextStepEnabled")
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Choose client spinner
        ClientSpinner(
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

        Box {
            // Baskets list
            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .padding(top = 20.dp)){

                when(currentStep){
                    1 -> {
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
                    2 -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                    .background(Jaune1)
                                    .padding(start = 10.dp)
                            ) {
                                Text("Paniers", textAlign = TextAlign.Start, style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        items(basketWrappers.filter { it.quantity > 0 }) {
                            Box(
                                Modifier.padding(8.dp)
                            ){
                                BasketItem(it, editMode = false)
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                                    .background(Jaune1)
                                    .padding(start = 10.dp)
                            ) {
                                Text("Voir plus", textAlign = TextAlign.Center, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            // Floating action buttons
            if(currentStep > 1) {
                FloatingActionButton(
                    onClick = {
                        // Change step if possible
                        currentStep -= 1
                    },
                    containerColor = if(firstNextStepEnabled) Purple80 else Gray1,
                    modifier = Modifier
                        .scale(0.6f)
                        .align(BottomStart),
                ) {
                    Icon(imageVector = Icons.Filled.ArrowBackIos, contentDescription = "")
                }
            }

            FloatingActionButton(
                onClick = {
                    // Change step if possible
                    Log.d("CreateCommand", "Step $currentStep")
                    if(firstNextStepEnabled){
                        currentStep += 1
                    }
                },
                containerColor = if(firstNextStepEnabled) Purple80 else Gray1,
                modifier = Modifier
                    .scale(0.6f)
                    .align(BottomEnd),
            ) {
                Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "")
            }
        }
    }
}

@Composable
@Preview
fun AddQuantity(
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

    Box {
        Box(
            modifier
                .padding(top = 10.dp)
                .fillMaxWidth(widthPercentage)
                //.height(35.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Jaune1)
                .clickable {
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
