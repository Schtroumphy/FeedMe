package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.toBasketDescription
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Purple40
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper.Companion.toWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.QuantityBubble

@Composable
fun BasketList(
    baskets: List<Basket> = listOf(),
    onClick : ((Basket) -> Unit)?= null
){

    Log.d("BasketList", "Baskets : $baskets")
    LazyColumn{
        items(baskets.map { it.toWrapper() }){
            BasketItem(it)
        }
    }
}

@Composable
@Preview
fun Button(){
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        tonalElevation = 12.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            Column(Modifier.padding(10.dp)) {
                Text("Ma première gare")
                Text("Ma deuxième gare")
            }
            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(topStart = 20.dp))
                .background(Purple40)
                .size(25.dp)

            ) {
                Icon(imageVector = Icons.Filled.ChevronRight, contentDescription = "", modifier = Modifier.align(Alignment.Center).size(18.dp).rotate(45f), tint = Color.White)
            }

        }
    }
}

@Composable
@Preview
fun BasketItem(
    basketWrapper: Wrapper<Basket> = Wrapper(quantity= 3, item = Basket(label = "Mon panier", wrappers = listOf(
        Wrapper(item = Product(label = "Banane", unitPrice = 12f), quantity = 2)
    ))),
    onClick : ((Basket)-> Unit)? = null,
    editMode : Boolean = true,
    onBasketQuantityChange : ((Pair<Long, Int>) -> Unit)? = null
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(vertical = 5.dp)
    ){
        Box(
            modifier = Modifier
                .weight(1.5f)
                .padding(5.dp)
        ){
            Image(painter = painterResource(id = R.drawable.fruits), contentDescription = "", modifier = Modifier
                .clip(RoundedCornerShape(15.dp)))
            QuantityBubble(modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(top = 8.dp), quantity = stringResource(id = R.string.euro, basketWrapper.item.price.toInt()),
                backgroundColor = Jaune1, padding = 5.dp)
        }

        Column(
            Modifier
                .padding(10.dp)
                .weight(4f)
        ) {
            Text(basketWrapper.item.label, fontWeight = FontWeight.Bold)
            Text(basketWrapper.item.wrappers.toBasketDescription(), fontWeight = FontWeight.Light, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if(editMode) AddQuantityBox(
            modifier = Modifier.weight(2.5f),
            quantity = basketWrapper.quantity,
            onQuantityChange = {
                onBasketQuantityChange?.invoke(basketWrapper.item.id to it)
            }
        ) else {
            Text("x ${basketWrapper.quantity}", fontWeight = FontWeight.Light, modifier = Modifier.padding(end = 10.dp))
        }
    }
}

