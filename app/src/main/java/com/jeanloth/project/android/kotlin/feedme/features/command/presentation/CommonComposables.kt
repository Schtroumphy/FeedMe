package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.*
import com.jeanloth.project.android.kotlin.feedme.features.dashboard.domain.FooterRoute
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.DialogType


@Composable
fun Header(
    context : Context,
    title : String = "Mon titre par défaut",
    onCloseOrBackClick : (() -> Unit)? = null,
    isBackAllowed : Boolean = false,
    displayBackOrClose: Boolean = false,
    displayAddButton : Boolean = true,
    addDialogType : DialogType? = null
){

    val showCustomDialogWithResult = rememberSaveable { mutableStateOf(false) }

    if(showCustomDialogWithResult.value){
        when(addDialogType){
            DialogType.ADD_CLIENT -> {
                AddClientDialog {
                    showCustomDialogWithResult.value = false
                    Log.d("TAG", "Create client : $it")
                    Toast.makeText(context, it, Toast.LENGTH_SHORT)
                }
            }
            else -> {
                showCustomDialogWithResult.value = false
            } // TODO Add other dialog types (add basket, product...)
        }
    }

    Row (horizontalArrangement = Arrangement.SpaceAround, modifier = Modifier.padding(20.dp)){
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
                showCustomDialogWithResult.value = true
            },
            containerColor = Bleu1,
            contentColor = White,
            modifier = Modifier.scale(0.7f)
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "")
        }
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
fun PersonName(modifier : Modifier = Modifier, name : String){
    Row(
      modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(imageVector = Icons.Filled.Person, contentDescription = "person")
        Text(name)
    }
}

@Composable
fun StatusCircle(color: Color, status : String){
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color))
        Text(status)
    }
}

@Composable
@Preview
fun QuantityBubble(quantity: Int = 20){
    Box(
        Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .background(White)
            .padding(2.dp)
    ){
        Text(quantity.toString(), modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
@Preview
fun PriceBox(modifier: Modifier = Modifier, color: Color = Orange1, price: String= "15€"){
    Box(
        modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ){
        Text(text = price, textAlign = TextAlign.Center)
    }
}

@Composable
@Preview
fun IconBox(modifier: Modifier = Modifier, color: Color = Orange1, icon: ImageVector = Icons.Filled.Edit){
    Box(
        modifier
            .padding(5.dp)
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ){
        Icon(imageVector = icon, tint = White, contentDescription = "icon button")
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
                    contentColor = White,
                    modifier =Modifier.scale(0.7f)
                ) {
                    Icon(imageVector = it.icon ?: Icons.Filled.Add, contentDescription = "")
                }
            } else {
                it.icon?.let { icon ->
                    Box(modifier =
                    Modifier
                        .clip(CircleShape)
                        .background(if (selectedRoute == it.name) White else Transparent)
                        .clickable {
                            selectedRoute = it.name
                            navController?.navigate(it.route)
                        }
                        .padding(8.dp)

                    ){
                        Icon(
                            imageVector = icon,
                            contentDescription = "",
                            tint = if(selectedRoute == it.name) Vert1 else DarkGray,
                        )
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun AppTextField(
    modifier : Modifier = Modifier,
    textState : MutableState<String>,
    widthPercentage : Float = 0.6f,
    @StringRes labelId : Int = R.string.label,
    keyboardType : KeyboardType = KeyboardType.Text,
    onTextEntered : ((String) -> Unit)? = null
)
{
    //var text by remember { mutableStateOf("") }
    val textFieldRequester = FocusRequester()
    val focusManager = LocalFocusManager.current

    Box(){
        Box(
            modifier
                .padding(top = 10.dp)
                .fillMaxWidth(widthPercentage)
                .height(35.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Jaune1)
                .clickable {
                    textFieldRequester.requestFocus()
                }
                .padding(10.dp)

        ) {
            BasicTextField(
                value = textState.value,
                onValueChange = {
                    textState.value = it
                },
                singleLine = true,
                textStyle = TextStyle(
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType
                ),
                keyboardActions = KeyboardActions (
                    onDone = {
                        focusManager.clearFocus()
                        onTextEntered?.invoke(textState.value)
                    }
                )
            )
        }
        Text(stringResource(id = labelId), modifier = Modifier.align(Alignment.TopStart))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
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
    addDialogType : DialogType? = null
){
    Scaffold(
        topBar = {
            if(displayHeader) {
                title?.let {
                    Header(context, it, onCloseOrBackClick, isBackAllowed, displayBackOrClose = displayBackOrClose, displayAddButton= displayAddButton, addDialogType = addDialogType)
                }
            }
        },
        bottomBar = {
            if(displayBottomNav) {
                Box(Modifier.fillMaxWidth()){
                    Footer(navController, modifier = Modifier.align(Alignment.Center), currentRoute)
                }
            }
        }
    ) { innerPadding ->
        content.invoke(innerPadding)
    }
}


@Composable
fun AddClientDialog(
    onNewClientAdded : ((String)-> Unit)? = null
) {
    val openDialog = remember { mutableStateOf(true) }
    val textState = rememberSaveable { mutableStateOf("") }
    var newClientName = ""

    if(openDialog.value){
        androidx.compose.material.AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = "Nouveau client")
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppTextField(
                        textState = textState,
                        labelId = R.string.firstname,
                        widthPercentage = 0.9f
                    ){
                        //textState = it
                    }
                }
            },
            buttons = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        text = "Valider",
                        onClickAction = {
                            onNewClientAdded?.invoke(textState.value)
                            openDialog.value = false
                        }
                    )
                }
            }
        )
    }
}