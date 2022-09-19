package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client.GetStringValueDialog

var mockClients = mutableListOf(
    AppClient(firstname = "Bella Rodriguez"),
    AppClient(firstname = "Isabelle Souris"),
    AppClient(firstname = "Jules TELON"),
    AppClient(firstname = "Iris CLAVIER"),
    AppClient(firstname = "Tony BILLARD"),
)
@Composable
@Preview
fun AddCommandPage(
    navController : NavController? = null
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Choose client spinner
        AppSpinner()

        Box {
            // Baskets saved list
            LazyColumn(modifier = Modifier
                .fillMaxHeight()
                .padding(top = 20.dp)){
                for(i in 1..4) {
                    item {
                        BasketItem()
                    }
                }
            }

            // Floating action button
            FloatingActionButton(
                onClick = {},
                modifier = Modifier.scale(0.6f).align(BottomEnd),
            ) {
                Icon(imageVector = Icons.Filled.ArrowForwardIos, contentDescription = "")
            }
        }
    }
}
@Composable
fun BasketItem(){
    Row (
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)
    ){
        Box(
            modifier = Modifier
                .weight(2f)
                .clip(RoundedCornerShape(15.dp))
        ){
            Image(painter = painterResource(id = R.drawable.fruits), contentDescription = "")
        }

        Column(
            Modifier
                .padding(10.dp)
                .weight(4f)
        ) {
            Text("Panier Gourmand", fontWeight = FontWeight.Bold)
            Text("Banane x1, Poireaux x2, Pommes de terre x3, Orange x3", fontWeight = FontWeight.Light, fontSize = 10.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)

        }
        AddQuantity(Modifier.weight(1.5f))
    }
}

@Composable
@Preview
fun AddQuantity(modifier: Modifier = Modifier){

    val interactionSource = remember { MutableInteractionSource() }
    val rippleColor = Color.Red

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically,
            modifier = modifier.clip(RoundedCornerShape(5.dp))
                .background(Gray1),
        ){
            Icon(imageVector = Icons.Filled.Remove, contentDescription = "", modifier = Modifier
                .clip(RoundedCornerShape(topStart = 5.dp, bottomStart = 5.dp))
                .clickable {

                }
                .padding(10.dp)
                .size(10.dp)
                .align(CenterVertically)
                .indication(
                    interactionSource = interactionSource,
                    indication = rememberRipple(color = rippleColor)
                )

            )
            Text(text = "5")
            Icon(imageVector = Icons.Filled.Add, contentDescription = "", modifier = Modifier
                .clip(RoundedCornerShape(topEnd = 10.dp, bottomEnd = 10.dp))
                .clickable {

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
fun AppSpinner(
    modifier : Modifier = Modifier,
    widthPercentage : Float = 0.6f,
    @StringRes labelId : Int = R.string.client
){
    val selectedItem = remember { mutableStateOf("")}
    val selectionMode = remember { mutableStateOf(false)}
    val showCustomDialogWithResult = remember { mutableStateOf(false) }

    if(showCustomDialogWithResult.value){
        GetStringValueDialog {
            showCustomDialogWithResult.value = false

            mockClients.add(
                AppClient(firstname = it)
            )
            selectedItem.value = it
        }
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
                }
                .padding(10.dp)

        ) {
            AnimatedVisibility(visible = selectionMode.value) {
                LazyColumn(
                    modifier = Modifier.height(150.dp)
                ){
                    items(mockClients){
                        Column(
                            modifier = Modifier.clickable {
                                selectedItem.value = it.toNameString()
                                selectionMode.value = false
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
                Text(selectedItem.value)
            }
        }
        Text(stringResource(id = labelId), modifier = Modifier.align(Alignment.TopStart))
    }
}
