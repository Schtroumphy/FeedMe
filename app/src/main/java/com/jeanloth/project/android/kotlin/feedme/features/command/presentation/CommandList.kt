package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.CommandStatus
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.PersonName
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.PriceBox
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.QuantityBubble
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.StatusCircle
import com.jeanloth.project.android.kotlin.feedme.R

/**
 * Show commands list sorted by delivery date block
 *
 * @param commands Map of deliveryDate formatted paired with command list
 */
@Composable
@Preview
fun CommandListPage(
    commands : Map<String, List<Command>> = mutableMapOf(), onClick: ((Long)-> Unit)? = null
){
    // List of commands
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.padding(15.dp).fillMaxSize()
    ) {

        commands.keys.forEach {
            // Delivery date
            item {
                // TODO Make this and statusText composable generic because same code
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically){
                    Box(
                        Modifier
                            .clip(CircleShape)
                            .size(10.dp)
                            .background(Orange1)
                    )
                    Text(it, style = MaterialTheme.typography.labelMedium, color = Orange1)
                }
                Spacer(Modifier.height(10.dp))
            }

            // Command summary
            items(commands[it] ?: listOf(Command())){
                CommandProductItem(it, onClick = {
                    onClick?.invoke(it)
                })
            }
        }
    }
}

@Composable
@Preview
fun CommandProductItem(command: Command = Command(), onClick: ((Long)-> Unit)? = null) {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = 3.dp,
        modifier = Modifier
            .padding(bottom = 5.dp)
            .border(
                width = 2.dp,
                shape = RoundedCornerShape(30.dp),
                color = command.status.color)
    ){
        Box {
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .clickable { onClick?.invoke(command.id) }
                    .padding(10.dp)
            ) {
                // Client Name + Status row
                CommandHeader(client = command.client.toNameString(), status = command.status.value, color = command.status.color)

                // List of baskets with quantity
                if(command.basketWrappers.isNotEmpty()) Text("Paniers", color = Orange1, modifier = Modifier.padding(top = 10.dp))
                Column(
                    Modifier.padding(horizontal = 10.dp)
                ) {
                    command.basketWrappers.forEach {
                        CommandBasket(it.item.label, quantity = it.quantity)
                    }
                }

                // List of products with quantity
                if(command.basketWrappers.isNotEmpty()) Text("Produits", color = Orange1, modifier = Modifier.padding(top = 10.dp))
                Column(
                    Modifier.padding(horizontal = 10.dp, vertical = if(command.basketWrappers.isNotEmpty()) 0.dp else 10.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ){
                    command.productWrappers.forEach {
                        CommandProduct(it.item.label, quantity = it.quantity)
                    }
                }
            }

        // Price box
        PriceBox(
            price = stringResource(id = R.string.euro, command.totalPrice),
            color = command.status.color,
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 20.dp)
        )
        }
    }
}

@Composable
@Preview
fun CommandProduct(productName: String = "Banane", quantity: Int = 2){
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantityBubble(quantity.toString(), backgroundColor = Gray1)
        Text(productName)
    }
}

@Composable
@Preview
fun CommandBasket(basketName: String = "Gourmandises", quantity: Int = 2){
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantityBubble(quantity.toString(), backgroundColor = Orange1)
        Text(basketName)
    }
}

@Composable
@Preview
fun CommandHeader(
    modifier: Modifier = Modifier,
    status: String = CommandStatus.LOADING.label,
    color: Color = CommandStatus.LOADING.color,
    client: String = "Albert MANCHON"
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        PersonName(name = client)
        StatusCircle(color, status)
    }
}