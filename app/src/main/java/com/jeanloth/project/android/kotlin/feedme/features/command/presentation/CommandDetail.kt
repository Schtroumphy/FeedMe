package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.SpaceEvenly
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Status
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.command.CommandVM

class CommandQuantityInfo(val newQuantity: Int, var basketId : Long = 0, var wrapperId : Long, val parentId : Long, var itemType : CommandItemType = CommandItemType.INDIVIDUAL_PRODUCT)
enum class CommandItemType {
    INDIVIDUAL_PRODUCT,
    BASKET
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
@Preview
fun CommandDetailPage(
    command : Command ?= null,
    onQuantityChange : ((CommandQuantityInfo)-> Unit)? = null
){
    val client = command?.client?.toNameString() ?: "Albert"

    // TODO Primary and second color in function of command status to transfer to all sub cmposables

    Scaffold(
        topBar = { CommandDetailHeader(client = client, command?.deliveryDate.formatToShortDate(), price = command?.totalPrice ?: 0, status = command?.status ?: Status.TO_DO) }
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
                                onQuantityChange?.invoke(it.apply {
                                    basketId = bw.id
                                    itemType = CommandItemType.BASKET
                                })
                            }
                        )
                    }
                }

                // Product list
                command?.productWrappers?.let {
                    item {
                        CommandBasketItem(
                            label = "Produits individuels",
                            productWrappers = it,
                            onQuantityChange = {
                                onQuantityChange?.invoke(it.apply {
                                    itemType = CommandItemType.INDIVIDUAL_PRODUCT
                                })
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CommandBasketItem(
    label : String? = "Panier Label",
    productWrappers : List<Wrapper<Product>> = emptyList(),
    onQuantityChange : ((CommandQuantityInfo)-> Unit)? = null
){
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
                .fillMaxHeight(0.8f)
                .width(10.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Orange1))
        }

        Column{
            label?.let {
                Text(it, style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(12.dp))
            }
            productWrappers.forEach {
                CommandProductItem(it, onQuantityChange = {
                    onQuantityChange?.invoke(it)
                })
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
    Row(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(productWrapper.item.label, style = MaterialTheme.typography.labelMedium, color = Color.LightGray, modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis)
        Text("${productWrapper.realQuantity} / ${productWrapper.quantity}", style = MaterialTheme.typography.labelMedium, color = Color.LightGray,modifier = Modifier.padding(end = 10.dp))
        if(isEditMode){
            AddQuantityBox(modifier = Modifier.width(80.dp),
                quantity = quantityEdit,
                onQuantityChange = {
                    quantityEdit = it
                    onQuantityChange?.invoke(CommandQuantityInfo(
                        newQuantity = it,
                        wrapperId = productWrapper.id,
                        parentId = productWrapper.parentId
                    ))
            })
        }
    }
}

@Composable
fun CommandAddress(
    address : String? = null,
    color: Color = Color.Black
){
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = Icons.Filled.LocationOn, contentDescription = "", tint = Color.LightGray)
        Text(address ?: "Adresse inconnue. Merci de la renseigner", style = MaterialTheme.typography.labelSmall, fontStyle = FontStyle.Italic, color = if(address == null) Color.LightGray else color)
    }
}

@Composable
fun SemiRoundedBox(
    text : String,
    textColor: Color = Color.White,
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
    address : String? = null
){
    Surface(
        shadowElevation = 2.dp,
        shape = RoundedCornerShape(bottomEnd = 20.dp, bottomStart = 20.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(15.dp),
            modifier = Modifier.padding(vertical = 10.dp),
        ) {

            // Client Name Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                contentAlignment = Alignment.Center
            ){
                ClientCommandBox(client)
                DateRoundedBox(modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 10.dp), deliveryDate)
            }

            // Delivery date rounded box
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                SemiRoundedBox(stringResource(id = R.string.euro, price))
                StatusText(status = status)
            }

            // Address
            CommandAddress(address)
        }
    }
}

@Composable
fun StatusText(
    modifier: Modifier = Modifier,
    status : Status
){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically){
        Box(
            Modifier
                .clip(CircleShape)
                .size(10.dp)
                .background(status.color))
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