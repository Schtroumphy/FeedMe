package com.jeanloth.project.android.kotlin.feedme.features.dashboard.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.core.theme.*
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute

enum class HomeCardType(val icon: ImageVector, val label: String, val color: Color){
    CA(Icons.Default.Create, "Chiffre d'affaire", BleuVert),
    CLIENT(Icons.Default.Person, "Clients", Jaune1),
    SALES(Icons.Default.ShoppingCart, "Ventes", Vert1),
    TODO(Icons.Default.List, "À faire", Rose1)
}

@Composable
@Preview
fun HomePage(
    navController: NavController? = null
){
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
                style = Typography.titleMedium
            )
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = "Settings",
                modifier = Modifier.size(20.dp)
            )
        }
        HomeCards(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .fillMaxHeight(0.9f),
            navController = navController !!
        )
    }
}

@Composable
fun HomeCards(
    modifier : Modifier,
    navController : NavController
){
    Row(
        modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ){

            HomeCard(
                onClick = {
                    navController?.navigate(FooterRoute.HOME.route)
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                cardType = HomeCardType.CA,
                count = 25)
            HomeCard(
                onClick = {
                    navController?.navigate(FooterRoute.COMMAND_LIST.route)
                },
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                cardType = HomeCardType.SALES,
                count = 25)
        }
        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            HomeCard(
                onClick = {
                    navController?.navigate(FooterRoute.PRODUCTS.route)
                },
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                cardType = HomeCardType.CLIENT,
                count = 5
            )
            HomeCard(
                onClick = {
                    navController?.navigate(FooterRoute.PRODUCTS.route)
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                cardType = HomeCardType.TODO
            )
        }
    }
}

@Composable
fun HomeCard(modifier: Modifier = Modifier, cardType: HomeCardType = HomeCardType.CA, count: Int = 10, onClick : (() -> Unit)? = null){
    Box(modifier = modifier
        .padding(10.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(cardType.color)
        .clickable {
            println("Click on ${cardType.label}")
            onClick?.invoke()
        }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            Icon(
                imageVector = cardType.icon,
                contentDescription = "",
                modifier = Modifier.size(20.dp),
                tint = Color.Black.copy(alpha = 0.6f)
            )
            Text(
                text = count.toString(),
                style = Typography.titleLarge,
                modifier = Modifier.padding(12.dp)
            )
            Text(text = cardType.label, fontStyle = FontStyle.Italic, color = Color.Black.copy(alpha = 0.6f))
        }
    }
}

