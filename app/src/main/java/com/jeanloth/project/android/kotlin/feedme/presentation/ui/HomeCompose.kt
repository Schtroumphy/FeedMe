package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.*

enum class HomeCardType(val icon: ImageVector, val label: String, val color: Color){
    CA(Icons.Default.Create, "Chiffre d'affaire", BleuVert),
    CLIENT(Icons.Default.Person, "Clients", Jaune1),
    SALES(Icons.Default.ShoppingCart, "Ventes", Vert1),
    TODO(Icons.Default.List, "À faire", Rose1)
}

@Composable
@Preview
fun HomePage(){
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Bonjour Axel,",
                style = Typography.titleLarge
            )
            Icon(
                painterResource(id = R.drawable.ic_filter),
                contentDescription = "Settings",
                modifier = Modifier.size(24.dp)
            )
        }
        HomeCards(Modifier)
    }
}

@Composable
fun HomeCards(modifier : Modifier){
    Row(
        modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.7f)
            .padding(5.dp)) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ){

            HomeCard(
                Modifier
                    .weight(2f)
                    .fillMaxWidth(), HomeCardType.CA, 25)
            HomeCard(
                Modifier
                    .weight(3f)
                    .fillMaxWidth(), HomeCardType.SALES, 25)
        }
        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            HomeCard(
                Modifier
                    .weight(3f)
                    .fillMaxWidth(), HomeCardType.CLIENT, 5)
            HomeCard(
                Modifier
                    .weight(2f)
                    .fillMaxWidth(), HomeCardType.TODO)
        }
    }
}

@Preview
@Composable
fun HomeCard(modifier: Modifier = Modifier, cardType: HomeCardType = HomeCardType.CA, count: Int = 10){
    Box(modifier = modifier
        .padding(10.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(cardType.color)
        .padding(17.dp)
    ){
        Box(modifier = Modifier
            .align(Alignment.Center)
            .fillMaxSize(0.8f)){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            ) {
                Icon(
                    imageVector = cardType.icon,
                    contentDescription = "",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = count.toString(),
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(12.dp)
                )
                Text(text = cardType.label, fontStyle = FontStyle.Italic)
            }
        }
    }
}