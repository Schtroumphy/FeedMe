package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.clearFocusOnKeyboardDismiss
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1
import com.jeanloth.project.android.kotlin.feedme.core.theme.RedDark
import com.jeanloth.project.android.kotlin.feedme.features.command.data.local.entities.ProductWrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Wrapper
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.QuantityBubble

@Composable
@Preview
fun RoundedText(modifier: Modifier = Modifier, text: String = "2"){
    Box(
        Modifier.clip(CircleShape).background(Color.White).border(width = 1.dp, color = Jaune1, shape = CircleShape).padding(horizontal = 8.dp, vertical = 5.dp)
    ){
        Text(text)
    }
}

@Composable
@Preview
fun RoundedText2(text: String = "2"){
    Box(
        Modifier.clip(CircleShape).border(width = 1.dp, color = Jaune1, shape = CircleShape).padding(horizontal = 8.dp, vertical = 5.dp)
    ){
        Text(text)
    }
}

@Composable
@Preview
fun RoundedProductItem(
    modifier: Modifier = Modifier,
    product: Product = Product(label = "Mon produit"),
    quantity: Int? = 0
){
    Box(Modifier){
        Image(
            painter = painterResource(product.imageId),
            contentDescription = "food icon",
            contentScale = ContentScale.Crop,// crop the image if it's not a square
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = Orange1,
                    shape = CircleShape
                )
        )
        RoundedText(text = quantity?.toString() ?: "0", modifier = Modifier.align(Alignment.TopStart))
    }
}

@Composable
@Preview
// /document/image%3A70 ou /document/image:70
fun ProductItem(
    modifier: Modifier = Modifier,
    product: Product = Product(label = "Mon produit"),
    quantity: Int? = null,
    onQuantityChange: ((Int?) -> Unit)? = null,
){
    var text by remember { mutableStateOf(if(quantity == null || quantity == 0) "" else quantity.toString()) }
    val focusManager = LocalFocusManager.current
    val textFieldRequester = FocusRequester()

    Box(
        modifier.fillMaxSize()
    ){
        Box(
            Modifier
                .align(Alignment.Center)
                .padding(15.dp)
                .fillMaxWidth()
                .height(110.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Gray1)
                .clickable {
                    textFieldRequester.requestFocus()
                }
                .padding(bottom = 10.dp, start = 5.dp, end = 5.dp)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ){
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
                        onQuantityChange?.invoke(if(it.isEmpty()) null else it.toInt())
                    },
                    shape = RoundedCornerShape(25.dp),

                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp
                    ),
                    modifier = Modifier
                        .focusRequester(textFieldRequester)
                        .clearFocusOnKeyboardDismiss()
                        .fillMaxWidth(0.7f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor =  Color.Transparent, //hide the indicator
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {  focusManager.clearFocus() }
                    )
                )
                Text(product.label, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){

            painterResource(product.imageId)?.let {
                Image(
                    painter = it,
                    contentDescription = "food icon",
                    contentScale = ContentScale.Crop,// crop the image if it's not a square
                    modifier = Modifier
                        .size(65.dp)
                        .clip(CircleShape)
                )
            }

            AnimatedVisibility(visible = text.isNotEmpty()) {
                FloatingActionButton(
                    shape = CircleShape,
                    containerColor = RedDark,
                    contentColor = Color.White,
                    onClick = {
                        text = ""
                        onQuantityChange?.invoke(null)
                    },
                    modifier = Modifier
                        .scale(0.45f)
                        .padding(end = 10.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "Clear")
                }
            }

        }

    }
}