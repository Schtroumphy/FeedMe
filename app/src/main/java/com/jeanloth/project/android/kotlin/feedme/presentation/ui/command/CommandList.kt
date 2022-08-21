package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.domain.models.CommandStatus
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Orange1

@Composable
@Preview
fun CommandListPage(){
    // List of commands
    LazyColumn(
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.padding(15.dp)
    ) {
        CommandStatus.values().forEach {
            item {
                CommandItem(it.label, it.color)
            }
        }
    }
}

@Composable
//@Preview
fun CommandItem(status: String, color: Color) {
    Box {
        Box(
            modifier = Modifier
                .padding(15.dp)
                .clip(RoundedCornerShape(30.dp))
                .background(Gray1)
                .border(width = 2.dp, shape = RoundedCornerShape(30.dp), color = color)
                .padding(15.dp)
        ){
            Column(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            ) {
                // Client Name + Status row
                CommandHeader(status = status, color = color)

                // List of product with quantity
                Column(
                    Modifier.padding(15.dp)
                ){
                    CommandProduct("Orange")
                    CommandProduct("Banane Jaune")
                    CommandProduct("Igname")
                }
            }
        }
        // Price box
        PriceBox(
            color = color,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 40.dp))
    }
}

@Composable
@Preview
fun CommandProduct(productName:String = "Banane"){
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        QuantityBubble(10)
        Text(productName)
    }
}

@Composable
@Preview
fun CommandHeader(
    modifier: Modifier = Modifier,
    status: String = CommandStatus.LOADING.label,
    color: Color = CommandStatus.LOADING.color
){
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        PersonName(name = "Albert MANCHON")
        StatusCircle(color, status)
    }
}