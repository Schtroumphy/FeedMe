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
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product

@Composable
@Preview
fun BasketList(
    baskets: List<Basket> = listOf(),
    onClick : ((Basket) -> Unit)?= null
){

    Log.d("BasketList", "Baskets : $baskets")
    LazyColumn{
        items(baskets){
            BasketItem(it)
        }
    }
}

@Composable
@Preview
fun BasketItem(
    basket: Basket = Basket(label = "Mon panier", wrappers = listOf(
        Wrapper(item = Product(label= "Produit1"), quantity = 2),
        Wrapper(item = Product(label= "Produit2"), quantity = 4),
    )),
    onClick : ((Basket)-> Unit)? = null,
    editMode : Boolean = false
){
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
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
            Text(basket.label, fontWeight = FontWeight.Bold)
            Text(basket.wrappers.toBasketDescription(), fontWeight = FontWeight.Light, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            //Text("Banane x1, Poireaux x2, Pommes de terre x3, Orange x3", fontWeight = FontWeight.Light, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(basket.price.toInt().toString() + "€", fontWeight = FontWeight.Bold)
        }
        if(editMode) AddQuantity(Modifier.weight(1.5f))
    }
}

