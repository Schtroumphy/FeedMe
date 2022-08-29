package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Red
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.AppTextField

enum class Product(@StringRes val label: Int, @DrawableRes val drawableId: Int){
    SWEET_POTATO(R.string.patate_douce, R.drawable.sweet_potato),
    BANANE_JAUNE(R.string.banane_jaune, R.drawable.delicious_banana),
    ORANGE(R.string.orange, R.drawable.orange),
    SWEET_POTATO2(R.string.patate_douce, R.drawable.sweet_potato),
    BANANE_JAUNE2(R.string.banane_jaune, R.drawable.delicious_banana),
    ORANGE2(R.string.orange, R.drawable.orange),
    SWEET_POTATO3(R.string.patate_douce, R.drawable.sweet_potato),
    BANANE_JAUNE3(R.string.banane_jaune, R.drawable.delicious_banana),
    ORANGE3(R.string.orange, R.drawable.orange),
    ORANGE4(R.string.orange, R.drawable.orange),
    SWEET_POTATO4(R.string.patate_douce, R.drawable.sweet_potato),
    BANANE_JAUNE4(R.string.banane_jaune, R.drawable.delicious_banana),
    ORANGE5(R.string.orange, R.drawable.orange),
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun BasketPage(
    navController : NavController? = null
){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        Box(Modifier.weight(0.6f)){
            AppTextField(textState = mutableStateOf(""))
        }
        Box(Modifier.weight(0.6f)) {
            AppTextField(
                textState = mutableStateOf(""),
                modifier = Modifier.align(Center),
                widthPercentage = 0.2f,
                labelId = R.string.price,
                keyboardType = KeyboardType.Number
            )
        }
        Box(
            Modifier.weight(8f)
                .fillMaxSize()
                .padding(15.dp)
        ){
            LazyVerticalGrid(
                cells = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                contentPadding = PaddingValues(bottom = 25.dp),
                modifier = Modifier.fillMaxWidth(0.85f).align(Center)
            ) {
                items(Product.values()){
                    ProductItem(it)
                }
            }
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp)
            ) {
                Icon(Icons.Filled.Check, contentDescription = "")
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product = Product.BANANE_JAUNE
){
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val textFieldRequester = FocusRequester()

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
                .padding(bottom = 10.dp, start = 5.dp, end= 5.dp)

        ) {
            Column(
                Modifier.fillMaxSize().align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                Text(stringResource(id = product.label), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }

        Image(
            painter = painterResource(product.drawableId),
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
                modifier = Modifier.padding(top = 10.dp).scale(0.3f)
            ) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Clear")
            }
        }


    }
}

@Composable
//@Preview
fun BasketTextField(
    title : String = "Champs de texte",
    modifier : Modifier = Modifier
){
    val text = remember { mutableStateOf( "") }
    var textState by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(modifier = modifier.padding(horizontal = 15.dp)){

    }
}

@Composable
//@Preview
fun InputTextField(
    labelText: String = "Libellé",
    modifier: Modifier = Modifier,
    dividerColor: Color = Color.Gray,
    dividerThickness: Dp = 1.dp,
    spacer: Dp = 10.dp,
    textStyle: TextStyle = TextStyle()
) {
    var value by remember { mutableStateOf(TextFieldValue("")) }
    val dividerState = remember { mutableStateOf(true) }
    BasicTextField(
        value = value,
        onValueChange = { value = it },
        modifier = modifier
            .onFocusChanged {
                dividerState.value = !it.isFocused
            },
        decorationBox = { innerTextField ->
            var mainModifier: Modifier = modifier
            if (!dividerState.value){
                mainModifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        shape = RoundedCornerShape(15.dp),
                        color = Color.LightGray
                    )
                    .padding(8.dp)
            }

            Column(
                modifier = mainModifier,
                content = {
                    Text(text = labelText, style = textStyle)
                    Spacer(modifier = Modifier.size(spacer))
                    innerTextField()
                    if (dividerState.value) {
                        Divider(
                            thickness = dividerThickness, color = dividerColor,
                            modifier = mainModifier
                        )
                    }
                }
            )
        }
    )
}