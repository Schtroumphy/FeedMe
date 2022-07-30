package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R

@Composable
@Preview
fun Header(
    title : String = "Mon titre par défaut",
    onCloseOrBackClick : (() -> Unit)? = null,
    isBackAllowed : Boolean = false
){
    Row (horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.padding(15.dp)){
        Icon(imageVector = if(isBackAllowed) Icons.Filled.ArrowBack else Icons.Filled.Close, contentDescription = "Close",
            modifier = Modifier.clickable { onCloseOrBackClick?.invoke() })
        Text(title, fontSize = 22.sp, textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.vertical_margin)), maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
@Preview
fun Button(text: String = "Valider", onClickAction: (() -> Unit)? = null){
    FilledTonalButton(
        onClick = { onClickAction?.invoke() },
        content = {
            Row (horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                Icon(Icons.Filled.Check, contentDescription = text)
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_margin)))
                Text(text)
            }
        }
    )
}

@Composable
@Preview
fun Footer(
    navController: NavController
){
    Row (horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 15.dp)
        .padding(
            bottom = dimensionResource(id = R.dimen.vertical_margin)
        )){
        Icon(imageVector = Icons.Filled.Home, contentDescription = "Home", modifier = Modifier.clickable { navController.navigate("home") })
        Icon(imageVector = Icons.Filled.List, contentDescription = "List")
        Icon(imageVector = Icons.Default.Person, contentDescription = "People", modifier = Modifier.clickable { navController.navigate("add_client"){
            popUpTo("home")
        } })
        Icon(imageVector = Icons.Filled.Close, contentDescription = "Products")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PageTemplate(
    displayHeader : Boolean = true,
    title: String= "Mon titre très long ?",
    content : @Composable () -> Unit,
    isBackAllowed : Boolean = false,
    navController: NavController,
    onCloseOrBackClick : (() -> Unit )? = null,
    displayBottomNav : Boolean = true
){
    Scaffold(
        topBar = {
            if(displayHeader) Header(title, onCloseOrBackClick, isBackAllowed)
        },
        bottomBar = {
            if(displayBottomNav) Footer(navController)
        }
    ) {
        content.invoke()
    }
}