package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import android.net.Uri
import android.util.Log
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopCenter
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.glide.rememberGlidePainter
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.clearFocusOnKeyboardDismiss
import com.jeanloth.project.android.kotlin.feedme.core.theme.*
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.product.Product
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AddProductDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AppTextField
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.GetIntValueDialog
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.QuantityBubble

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun CreateBasketPage(
    products: List<Product> = listOf(Product(label = "Mon orange"), Product(label = "Poire"), Product(label = "Pomme")),
    onValidateBasket : ((String, Int, Map<Product, Int?>) -> Unit)?= null,
    onAddProduct : ((String, Uri?)-> Unit)?= null
){
    var label by remember { mutableStateOf("") }
    val productQuantity = remember { mutableStateMapOf<Product, Int?>() }
    var selectedPrice by remember { mutableStateOf(0)}
    var customQuantity by remember { mutableStateOf(-1)}
    val quantities = listOf(10, 15, 20, 25, customQuantity)
    val validationEnabled = label.isNotEmpty() && selectedPrice != 0 && !productQuantity.isEmpty()

    val showCustomDialogWithResult = rememberSaveable { mutableStateOf(false) }

    if(showCustomDialogWithResult.value){
        GetIntValueDialog {
            showCustomDialogWithResult.value = false
            customQuantity = it
            selectedPrice = it
        }
    }

    val showAddProductDialog = rememberSaveable { mutableStateOf(false) }
    val showImageUriTest = rememberSaveable { mutableStateOf(false) }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    if(showAddProductDialog.value){
        AddProductDialog { name, uri ->
            name?.let {
                onAddProduct?.invoke(it, uri)
                //imageUri.value = uri
                //showImageUriTest.value = true
            }
            showAddProductDialog.value = false
        }
    }

    if(showImageUriTest.value){
        Image(
            painter = rememberGlidePainter(
                request = imageUri,
                shouldRefetchOnSizeChange = { _, _ -> false },
            ),
            contentDescription = null,
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally
    ) {
        Box(Modifier.weight(0.6f)){
            AppTextField(
                onTextEntered = {
                    label = it
                }
            )
        }
        Box(
            Modifier
                .padding(top = 10.dp)
                .weight(1f)) {
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
                verticalArrangement = Arrangement.Center,
                contentPadding = PaddingValues(bottom = 25.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(TopCenter)
            ) {

                items(products){ product ->
                    ProductItem(
                        product,
                        onQuantityChange = {
                            Log.d("Create Basket", "Quantity received : $it")
                            if(it == null) productQuantity.remove(product) else productQuantity.put(product, it)
                        }
                    )
                }

                item {
                    Box(
                        Modifier
                            .padding(15.dp)
                            .fillMaxWidth()
                            .height(110.dp)
                            .clickable {
                                showAddProductDialog.value = true
                            },
                        contentAlignment = Center
                    ){
                        FloatingActionButton(
                            onClick = {
                                Log.d("CreateBasket", "Click to add product")
                                showAddProductDialog.value = true
                          },
                            containerColor = BleuVert,
                            modifier = Modifier
                                .scale(0.8f)
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "")
                        }
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    if(validationEnabled){
                        onValidateBasket?.invoke(label, selectedPrice, productQuantity)
                    } else {
                        Log.d("CreateBasket", "Not enough element - Label : $label, Price : $selectedPrice, map is empty: ${productQuantity.isEmpty()}")
                    }
                },
                containerColor = if(validationEnabled) Purple80 else Color.LightGray,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(10.dp)
            ) {
                Icon(Icons.Filled.Check, contentDescription = "")
            }

        }
    }
}

@Composable
@Preview
// /document/image%3A70 ou /document/image:70
fun ProductItem(
    product: Product = Product(label = "Mon produit"),
    onQuantityChange : ((Int?)-> Unit)? = null
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

            Image(
                painter = painterResource(product.imageId),
                contentDescription = "food icon",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .size(65.dp)
                    .clip(CircleShape)
            )

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
