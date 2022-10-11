package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.clearFocusOnKeyboardDismiss
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Red
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AppTextField
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.GetIntValueDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.QuantityBubble
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.products.ProductVM

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BasketPage(
    productVM: ProductVM,
    context: Context
){
    val products by productVM.products.collectAsState()
    var selectedPrice by remember { mutableStateOf(0)}
    var customQuantity by remember { mutableStateOf(-1)}
    val quantities = listOf(10, 15, 20, 25, customQuantity)

    val showCustomDialogWithResult = remember { mutableStateOf(false) }

    if(showCustomDialogWithResult.value){
        GetIntValueDialog {
            showCustomDialogWithResult.value = false
            customQuantity = it
            selectedPrice = it
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        Box(Modifier.weight(0.6f)){
            AppTextField()
        }
        Box(Modifier.weight(0.6f)) {
            Row(
                Modifier
                    .fillMaxSize()
                    .align(Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Text(stringResource(id = R.string.price))
                quantities.forEach {
                    QuantityBubble( it, if(selectedPrice == it) Jaune1 else Gray1, 12.dp){ price ->
                        if(it == customQuantity) {
                            showCustomDialogWithResult.value = true
                        }
                        selectedPrice = price
                    }
                }

                Text("€")
            }
        }
        Box(
            Modifier
                .weight(8f)
                .fillMaxSize()
                .padding(15.dp)
        ){
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(bottom = 25.dp),
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .align(Center)
            ) {
                items(products){
                    ProductItem(it, context)
                }
            }
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
                    .scale(0.6f)
            ) {
                Icon(Icons.Filled.Check, contentDescription = "")
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    context: Context
){
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val textFieldRequester = FocusRequester()

    val imageResource: Int = context.resources.getIdentifier(product.image, "drawable", context.packageName)

    Box(
        Modifier.fillMaxSize()
    ){
        Box(
            Modifier
                .align(Center)
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
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ){
                TextField(
                    value = text,
                    onValueChange = {
                        text = it
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

        Image(
            painter = painterResource(imageResource),
            contentDescription = "food icon",
            contentScale = ContentScale.Crop,            // crop the image if it's not a square
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(65.dp)
                .clip(CircleShape)
        )

        AnimatedVisibility(visible = text.isNotBlank(), modifier = Modifier.align(Alignment.BottomEnd)) {
            FloatingActionButton(
                shape = CircleShape,
                containerColor = Red,
                contentColor = Color.White,
                onClick = {
                    text = ""
                },
                modifier = Modifier
                    .padding(top = 10.dp)
                    .scale(0.3f)
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Clear")
            }
        }
    }
}
