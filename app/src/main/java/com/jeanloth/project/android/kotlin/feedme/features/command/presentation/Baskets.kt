package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.toBasketDescription
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Basket
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper.Companion.toWrapper

@Composable
@Preview
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
fun BasketItem(
    basketWrapper: Wrapper<Basket>,
    onClick : ((Basket)-> Unit)? = null,
    editMode : Boolean = false,
    onBasketQuantityChange : ((Pair<Long, Int>) -> Unit)? = null
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(vertical = 5.dp)
    ){
        Box(
            modifier = Modifier
                .weight(1.5f)
                .clip(RoundedCornerShape(15.dp))
        ){
            Image(painter = painterResource(id = R.drawable.fruits), contentDescription = "")
        }

        Column(
            Modifier
                .padding(10.dp)
                .weight(4f)
        ) {
            Text(basketWrapper.item.label, fontWeight = FontWeight.Bold)
            Text(basketWrapper.item.wrappers.toBasketDescription(), fontWeight = FontWeight.Light, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(basketWrapper.item.price.toInt().toString() + "€", fontWeight = FontWeight.Bold)
        }
        if(editMode) AddQuantityBox(
            modifier = Modifier.weight(2.5f),
            quantity = basketWrapper.quantity,
            onQuantityChange = {
                onBasketQuantityChange?.invoke(basketWrapper.item.id to it)
            }
        ) else {
            Text("x ${basketWrapper.quantity}")
        }
    }
}

