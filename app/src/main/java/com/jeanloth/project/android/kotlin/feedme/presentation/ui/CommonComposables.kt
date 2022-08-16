package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.presentation.FooterRoute
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.BleuVert
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Red
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Vert1

@Composable
@Preview
fun Header(
    title : String = "Mon titre par défaut",
    onCloseOrBackClick : (() -> Unit)? = null,
    isBackAllowed : Boolean = false
){
    Row (horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.padding(top = 15.dp, start = 15.dp, end = 15.dp)){
        Icon(imageVector = if(isBackAllowed) Icons.Filled.ArrowBack else Icons.Filled.Close, contentDescription = "Close",
            modifier = Modifier.clickable { onCloseOrBackClick?.invoke() })
        Text(title, fontSize = 22.sp, textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(id = R.dimen.vertical_margin)), maxLines = 1, overflow = TextOverflow.Ellipsis)
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
fun AppIconButton(icon: ImageVector, onClickAction: (() -> Unit)? = null){
    FilledTonalButton(
        onClick = { onClickAction?.invoke() },
        content = {
            Icon(icon, contentDescription = "icon button")
        }
    )
}

@Composable @Preview
fun Footer(
    navController: NavController? = null
){
    var selectedRoute by remember { mutableStateOf("") }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(BleuVert)
            .padding(vertical = 2.dp)
    ){
        FooterRoute.values().filter { it.inFooter }.forEach {
            if(it.actionButton){
                FloatingActionButton(
                    onClick = {
                        selectedRoute = it.name
                        navController?.navigate(it.route)
                    }
                ) {
                    Icon(imageVector = it.icon ?: Icons.Filled.Add, contentDescription = "")
                }
            } else {
                it.icon?.let { icon ->
                    Box(modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(if (selectedRoute == it.name) White else BleuVert)
                        .padding(8.dp)
                    ){
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            tint = if(selectedRoute == it.name) Vert1 else White,
                            modifier = Modifier.clickable {
                                selectedRoute = it.name
                                navController?.navigate(it.route)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun PageTemplate(
    displayHeader : Boolean = true,
    title: String= "Mon titre très long ?",
    content : @Composable (PaddingValues) -> Unit,
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
    ) { innerPadding ->
        content.invoke(innerPadding)
    }
}