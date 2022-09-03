package com.jeanloth.project.android.kotlin.feedme.presentation.ui

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1

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
fun AppTextField(
    modifier : Modifier = Modifier,
    textState : MutableState<String>,
    widthPercentage : Float = 0.6f,
    @StringRes labelId : Int = R.string.label,
    keyboardType : KeyboardType = KeyboardType.Text,
    onTextEntered : ((String) -> Unit)? = null
)
{
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
                    //textFieldRequester.requestFocus()
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
                    .align(Alignment.Center)
                    .focusRequester(textFieldRequester),
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

    DisposableEffect(Unit) {
        textFieldRequester.requestFocus()
        onDispose { }
    }
}

@Composable
fun NoDataText(@StringRes res: Int, modifier : Modifier = Modifier){
    Text(stringResource(id = res), modifier = modifier.fillMaxSize(), textAlign = TextAlign.Center)
}
