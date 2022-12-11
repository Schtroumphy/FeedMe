package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.Bleu1
import com.jeanloth.project.android.kotlin.feedme.core.theme.BleuVert
import com.jeanloth.project.android.kotlin.feedme.core.theme.Vert1
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AddButtonActionType
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PageTemplate(
    context: Context,
    title: String? = null,
    content: @Composable (PaddingValues) -> Unit,
    isBackAllowed: Boolean = false,
    navController: NavController,
    onCloseOrBackClick: (() -> Unit)? = null,
    displayHeader: Boolean = true,
    displayBottomNav: Boolean = true,
    displayBackOrClose: Boolean = true,
    displayAddButton: Boolean = false,
    currentRoute: FooterRoute?,
    addButtonActionType : AddButtonActionType? = null,
    onNewClientAdded: ((String) -> Unit)? = null,
    onDialogDismiss: (() -> Unit)? = null
){
    Scaffold(
        topBar = {
            if(displayHeader) {
                title?.let {
                    Header(context, it, onCloseOrBackClick, isBackAllowed, displayBackOrClose = displayBackOrClose, displayAddButton= displayAddButton, addButtonActionType = addButtonActionType,
                        onNewClientAdded = {
                            onNewClientAdded?.invoke(it)
                        },
                        onAddBasketClicked = {
                            navController.navigate(FooterRoute.ADD_BASKET.route)
                        },
                        onDialogDismiss = {
                            onDialogDismiss?.invoke()
                        }

                    )
                }
            }
        },
        bottomBar = {
            if(displayBottomNav) {
                Box(Modifier.fillMaxWidth().background(Color.White)){
                    Footer(navController, modifier = Modifier.align(Alignment.Center), currentRoute)
                }
            }
        }
    ) { innerPadding ->
        content.invoke(innerPadding)
    }
}


@Composable
fun Header(
    context : Context,
    title : String = "Mon titre par défaut",
    onCloseOrBackClick : (() -> Unit)? = null,
    isBackAllowed : Boolean = false,
    displayBackOrClose: Boolean = false,
    displayAddButton : Boolean = true,
    addButtonActionType : AddButtonActionType? = null,
    onNewClientAdded: ((String) -> Unit)? = null,
    onAddBasketClicked : (()-> Unit)? = null,
    onDialogDismiss : (()-> Unit)? = null
){
    val showCustomDialogWithResult = rememberSaveable { mutableStateOf(false) }

    if(showCustomDialogWithResult.value){
        when(addButtonActionType){
            AddButtonActionType.ADD_CLIENT -> {
                GetStringValueDialog (
                        onNewClientAdded = {
                        showCustomDialogWithResult.value = false
                        Log.d("TAG", "Create client : $it")
                        Toast.makeText(context, it, Toast.LENGTH_SHORT)

                        // Save client to db
                        onNewClientAdded?.invoke(it)
                    },
                    onDismiss = {
                        onDialogDismiss?.invoke()
                    }
                )
            }
            else -> {}
        }
    }

    Row (horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)){
        if(displayBackOrClose) Icon(imageVector = if(isBackAllowed) Icons.Filled.ArrowBack else Icons.Filled.Close, contentDescription = "Close",
            modifier = Modifier
                .clip(CircleShape)
                .clickable { onCloseOrBackClick?.invoke() })
        Text(title, fontSize = 22.sp, textAlign = TextAlign.Center,
            modifier = Modifier
                .weight(1f)
                .padding(top = dimensionResource(id = R.dimen.vertical_margin)), maxLines = 1, overflow = TextOverflow.Ellipsis)
        if(displayAddButton) FloatingActionButton(
            onClick = {
                when(addButtonActionType){
                    AddButtonActionType.ADD_CLIENT -> showCustomDialogWithResult.value = true
                    AddButtonActionType.ADD_BASKET -> onAddBasketClicked?.invoke()
                    null -> TODO()
                }

            },
            containerColor = Bleu1,
            contentColor = Color.White,
            modifier = Modifier.scale(0.7f)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        }
    }
}


@Composable
fun Footer(
    navController: NavController? = null,
    modifier: Modifier = Modifier,
    route: FooterRoute?
){
    var selectedRoute by remember { mutableStateOf(route?.route) }
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier
            .fillMaxWidth(0.8f)
            .padding(bottom = 15.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(BleuVert.copy(alpha = 0.2f))
    ){
        FooterRoute.values().filter { it.inFooter }.forEach {
            if(it.actionButton){
                FloatingActionButton(
                    onClick = {
                        selectedRoute = it.name
                        navController?.navigate(it.route)
                    },
                    containerColor = Bleu1,
                    contentColor = Color.White,
                    modifier =Modifier.scale(0.7f)
                ) {
                    Icon(imageVector = it.icon ?: Icons.Filled.Add, contentDescription = "")
                }
            } else {
                it.icon?.let { icon ->
                    Box(modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(if (selectedRoute == it.name) Color.White else Color.Transparent)
                        .clickable {
                            selectedRoute = it.name
                            navController?.navigate(it.route)
                        }
                        .padding(8.dp)

                    ){
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            tint = if(selectedRoute == it.name) Vert1 else Color.DarkGray,
                        )
                    }
                }
            }
        }
    }
}
