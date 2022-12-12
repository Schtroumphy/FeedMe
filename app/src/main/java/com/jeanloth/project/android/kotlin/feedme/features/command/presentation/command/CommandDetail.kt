package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.feedme.core.extensions.progession
import com.jeanloth.project.android.kotlin.feedme.core.extensions.toQuantityEditColor
import com.jeanloth.project.android.kotlin.feedme.core.theme.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command.Companion.toString2
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.AddQuantityBox
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AppTextField
import kotlinx.coroutines.launch

data class CommandQuantityInfo(val newQuantity: Int, var basketId : Long = 0, var wrapperId : Long, val parentId : Long, var wrapperType : WrapperType = WrapperType.COMMAND_INDIVIDUAL_PRODUCT){
    override fun toString(): String {
        return "Infos | newQuantity : $newQuantity, basketId : $basketId, wrapperId : $wrapperId, parentId : $parentId, wrapperType : ${wrapperType.name}"
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun CommandDetailPage(
    commandDetailVM : CommandDetailsVM
){
    val command by commandDetailVM.currentCommand.collectAsState(null)
    val client = command?.client?.toNameString() ?: "Albert"

    val predictions by commandDetailVM.predictions

    Log.d("Details", "Command ${command.toString2()}")

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(sheetState.isVisible) {
        coroutineScope.launch { sheetState.hide() }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = { BottomSheet(
            predictions = predictions,
            onAddressValidate = { address ->
                commandDetailVM.updateCommandAddress(address)
                coroutineScope.launch {
                    sheetState.hide()
                }
            },
            onAddressChange = {
                commandDetailVM.getPredictions(it)
            }
        ) },
        modifier = Modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Scaffold(
            topBar = {
                CommandDetailHeader(
                    client = client,
                    deliveryDate = command?.deliveryDate.formatToShortDate(),
                    price = command?.totalPrice ?: 0,
                    status = command?.status ?: Status.TO_DO,
                    address = command?.deliveryAddress,
                    onAddressClick = {
                        // TODO If command address unknown, display bottom sheet to add it, else display map
                        coroutineScope.launch {
                            if (sheetState.isVisible) sheetState.hide()
                            else sheetState.show()
                        }
                    }
                )
            }, bottomBar = {
                ActionCommandDetailButton(command?.status) { action ->
                    commandDetailVM.onDetailActionClick(action)
                }
            }
        ) {
            CompositionLocalProvider(
                LocalOverScrollConfiguration provides null
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .padding(top = 30.dp, start = 15.dp, end = 15.dp),
                    verticalArrangement = Arrangement.spacedBy(30.dp)
                ) {

                    // Basket list
                    command?.basketWrappers?.forEach { bw ->
                        item {
                            CommandBasketItem(
                                label = bw.item.label,
                                productWrappers = bw.item.wrappers,
                                onQuantityChange = {
                                    commandDetailVM.updateRealCommandQuantity(it.apply {
                                        basketId = bw.id
                                    })
                                },
                                status = command?.status ?: Status.TO_DO
                            )
                        }
                    }

                    // Product list
                    command?.productWrappers?.let {
                        item {
                            CommandBasketItem(
                                label = stringResource(id = R.string.individual_products),
                                productWrappers = it,
                                onQuantityChange = {
                                    commandDetailVM.updateRealCommandQuantity(it)
                                },
                                status = command?.status ?: Status.TO_DO
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BottomSheet(
    predictions: List<String>,
    address: String = "",
    validateButtonColor: Color = Orange1,
    onAddressChange: ((String) -> Unit)? = null,
    onAddressValidate: ((String) -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var selectedAddress by remember { mutableStateOf(address) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Saisir l'adresse de livraison",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.align(Start)
        )
        Spacer(modifier = Modifier.height(30.dp))
        AppTextField(
            initialValue = selectedAddress,
            modifier = Modifier
                .fillMaxWidth()
                .align(CenterHorizontally)
                .padding(bottom = 35.dp),
            widthPercentage = 1f,
            labelId = R.string.delivery_address,
            displayLabelText = false,
            autoFocus = false,
            onTextEntered = {
                keyboardController?.hide()
            },
            onValueChange = {
                onAddressChange?.invoke(it)
            }
        )
        Column(
            modifier = Modifier.fillMaxHeight(0.7f).fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = CenterHorizontally
        ) {
            predictions.forEach { prediction ->
                Text(
                    text = prediction,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .clickable {
                            selectedAddress = prediction

                            // Close keyboard
                            keyboardController?.hide()
                        },
                    overflow = TextOverflow.Clip
                )
                Divider(color =  Gray1, thickness = 1.5.dp)
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .scale(0.7f)
                .align(End),
            containerColor = validateButtonColor,
            onClick = {
                onAddressValidate?.invoke(selectedAddress)
            }
        ) {
            Icon(
                tint = White,
                imageVector = Icons.Rounded.Check,
                contentDescription = ""
            )
        }
    }
}

@Composable
@Preview
fun ActionCommandDetailButton(currentStatus : Status? = Status.TO_DO, onClick : ((CommandAction) -> Unit)? = null){
    currentStatus?.potentialAction?.let { action ->
        Column(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FloatingActionButton(
                modifier = Modifier.scale(0.7f),
                containerColor = currentStatus.primaryColor,
                onClick = {
                    onClick?.invoke(action)
                }
            ) {
                Icon(
                    tint = White,
                    imageVector = action.iconButton,
                    contentDescription = ""
                )
            }
            Text(stringResource(id = action.detailText), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
fun CommandBasketItem(
    label : String? = "Panier Label",
    productWrappers : List<Wrapper<Product>> = emptyList(),
    onQuantityChange : ((CommandQuantityInfo)-> Unit)? = null,
    status : Status = Status.TO_DO
){

    val progress = productWrappers.progession()

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box{
            Box(modifier = Modifier
                .fillMaxHeight()
                .width(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Gray1)
            )
            Box(modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxHeight(if (progress.isNaN()) 0f else progress)
                .width(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(status.secondaryColor))
        }

        Column{
            label?.let {
                Text(it, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(12.dp))
            }
            productWrappers.forEach {
                CommandProductItem(
                    isEditMode = status.order < Status.DELIVERING.order, // Can edit quantities only if not delivered yet
                    productWrapper = it,
                    onQuantityChange = { info ->
                        onQuantityChange?.invoke(info)
                    }
                )
            }
        }
    }
}

@Composable
fun CommandProductItem(
    productWrapper : Wrapper<Product>,
    isEditMode : Boolean = true,
    onQuantityChange : ((CommandQuantityInfo)-> Unit)? = null
){
    var quantityEdit by remember { mutableStateOf(productWrapper.realQuantity) }
    var backgroundColor by remember { mutableStateOf(Gray1) }

    quantityEdit = productWrapper.realQuantity
    backgroundColor = quantityEdit.toQuantityEditColor(productWrapper.quantity)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = productWrapper.item.label,
            style = MaterialTheme.typography.labelMedium,
            color = Color.LightGray,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${productWrapper.realQuantity} / ${productWrapper.quantity}",
            style = MaterialTheme.typography.labelMedium,
            color = Color.LightGray,
            modifier = Modifier.padding(end = 10.dp)
        )
        if(isEditMode){
            AddQuantityBox(modifier = Modifier.width(80.dp),
                quantity = quantityEdit,
                backgroundColor = backgroundColor,
                onQuantityChange = {
                    quantityEdit = it
                    backgroundColor = quantityEdit.toQuantityEditColor(productWrapper.quantity)
                    onQuantityChange?.invoke(CommandQuantityInfo(
                        newQuantity = it,
                        wrapperId = productWrapper.id,
                        parentId = productWrapper.parentId,
                        wrapperType = productWrapper.wrapperType
                    ))
            })
        }
    }
}

@Composable
fun CommandAddress(
    address : String? = null,
    color: Color = Black,
    onAddressClick: (() -> Unit)?
){
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 10.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                onAddressClick?.invoke()
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Filled.LocationOn,
            contentDescription = "",
            tint = Color.LightGray
        )
        Text(
            text = if(address.isNullOrEmpty()) stringResource(id = R.string.unknown_address) else address,
            style = MaterialTheme.typography.labelSmall,
            fontStyle = FontStyle.Italic,
            color = if (address == null) Color.LightGray else color
        )
    }
}

@Composable
fun SemiRoundedBox(
    text : String,
    textColor: Color = White,
    backgroundColor : Color = Orange1
){
    Text(
        text,
        color = textColor,
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier
            .clip(RoundedCornerShape(topEnd = 15.dp, bottomEnd = 15.dp))
            .background(backgroundColor)
            .padding(horizontal = 20.dp, vertical = 3.dp)
    )
}

@Composable
@Preview
fun CommandDetailHeader(
    client : String = "Albert MANCHON",
    deliveryDate : String = "23/11/2022",
    status : Status = Status.IN_PROGRESS,
    price : Int = 19,
    address : String? = null,
    onAddressClick : (()-> Unit)? = null
){
    Surface(
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(vertical = 10.dp),
        ) {

            // Client Name Box + Delivery date rounded box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ){
                ClientCommandBox(client)
                DateRoundedBox(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .offset(y = 10.dp), deliveryDate,
                    backgroundColor = status.secondaryColor
                )
            }

            // Rounded price + Status text in row
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = SpaceBetween
            ){
                SemiRoundedBox(stringResource(id = R.string.euro, price), backgroundColor = status.secondaryColor, textColor = if(status != Status.TO_DO) White else Black)
                StatusText(status = status)
            }

            // Address
            CommandAddress(address){
                onAddressClick?.invoke()
            }
        }
    }
}

@Composable
fun StatusText(
    modifier: Modifier = Modifier,
    status : Status
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            Modifier
                .clip(CircleShape)
                .size(10.dp)
                .background(status.secondaryColor))
        Text(status.value, style = MaterialTheme.typography.labelMedium, fontStyle = FontStyle.Italic)
    }
}

@Composable
fun DateRoundedBox(
    modifier: Modifier = Modifier,
    date : String,
    backgroundColor: Color = Jaune1
){
    Text(date, modifier= modifier
        .clip(RoundedCornerShape(20.dp))
        .background(backgroundColor)
        .padding(horizontal = 8.dp, vertical = 5.dp), style = MaterialTheme.typography.labelSmall
    )
}

@Composable
fun ClientCommandBox(
    client: String = "Albert MARCHONS"
){
    Box(
        Modifier
            .fillMaxWidth(0.7f)
            .height(70.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(Gray1)

    ){
        Text(client, modifier = Modifier.align(Alignment.Center))
    }
}