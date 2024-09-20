package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common

import android.app.DatePickerDialog
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import android.widget.DatePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.extensions.SLASH_DATE_FORMAT
import com.jeanloth.project.android.kotlin.feedme.core.extensions.clearFocusOnKeyboardDismiss
import com.jeanloth.project.android.kotlin.feedme.core.extensions.formatToShortDate
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Purple80
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun Button(text: String = stringResource(id = R.string.validate), onClickAction: (() -> Unit)? = null) {
    FilledTonalButton(
        onClick = { onClickAction?.invoke() },
        content = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Filled.Check, contentDescription = text)
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.small_margin)))
                Text(text)
            }
        }
    )
}

@Composable
fun PersonName(modifier: Modifier = Modifier, name: String) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Filled.Person, contentDescription = "person")
        Text(name)
    }
}

@Composable
fun StatusCircle(color: Color, status: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(status)
    }
}

@Composable
@Preview
fun PriceBubble(
    modifier: Modifier = Modifier,
    price: String = "20€",
    backgroundColor: Color = White,
    padding: Dp = 2.dp,
    size: Dp = 22.dp,
) {
    Box(modifier = modifier
        .fillMaxSize(0.4f)
        .clip(RoundedCornerShape(topStart = 8.dp))
        .background(backgroundColor)
        //.size(25.dp)
    ) {
        Text(price, style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.Center))
    }
}

// TODO Create specific Price bubble
@Composable
@Preview
fun QuantityBubble(
    modifier: Modifier = Modifier,
    quantity: String = "20",
    backgroundColor: Color = White,
    padding: Dp = 2.dp,
    size: Dp = 22.dp,
    onClick: ((Int) -> Unit)? = null,
) {
    val focusManager = LocalFocusManager.current
    val text = if ((quantity.filterNot { it == '€' }.toIntOrNull() ?: 0) > 0) quantity else "?"

    Box(
        modifier
            .size(size)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                focusManager.clearFocus() // TODO Really necessary ?
                onClick?.invoke(quantity.toIntOrNull() ?: 0)
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun PriceBox(modifier: Modifier = Modifier, color: Color = Orange1, price: String = "15€", shape : Shape = RoundedCornerShape(15.dp)) {
    Box(
        modifier
            .clip(shape)
            .background(color)
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Text(text = price, textAlign = TextAlign.Center)
    }
}

@Composable
fun IconBox(
    modifier: Modifier = Modifier,
    color: Color = Orange1,
    icon: ImageVector = Icons.Filled.Edit,
    onClick : (()-> Unit)? = null
) {
    Box(
        modifier
            .padding(5.dp)
            .clip(CircleShape)
            .background(color)
            .clickable {
                onClick?.invoke()
            }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(imageVector = icon, tint = White, contentDescription = "icon button")
    }
}


@Composable
@Preview
fun AppTextField(
    modifier: Modifier = Modifier,
    initialValue : String = "",
    widthPercentage: Float = 0.6f,
    @StringRes labelId: Int = R.string.label,
    keyboardType: KeyboardType = KeyboardType.Text,
    displayLabelText : Boolean = true,
    autoFocus : Boolean = true,
    onValueChange: ((String) -> Unit)? = null,
    onTextEntered: ((String) -> Unit)? = null
) {
    var text by remember { mutableStateOf(initialValue) }
    LaunchedEffect(key1 = initialValue ){
        text = initialValue
    }
    val textFieldRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(modifier) {
        Box(
            Modifier
                .padding(top = 10.dp)
                .fillMaxWidth(widthPercentage)
                .clip(RoundedCornerShape(20.dp))
                .background(Jaune1)
                .align(Alignment.Center)
                .padding(6.dp)
        ) {
            BasicTextField(
                value = (text),
                onValueChange = {
                    text = it
                    onValueChange?.invoke(text)
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                modifier =
                Modifier
                    .align(Alignment.Center)
                    .focusRequester(textFieldRequester)
                    .clearFocusOnKeyboardDismiss(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    capitalization = KeyboardCapitalization.Words
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        onTextEntered?.invoke(text)
                    }
                )
            )
        }
        if(displayLabelText) Text(
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.TopStart)
        )
    }

    LaunchedEffect(autoFocus){
        delay(100)
        if(autoFocus) textFieldRequester.requestFocus()
    }
}

@Composable
fun NoDataText(@StringRes res: Int, modifier: Modifier = Modifier) {
    Text(stringResource(id = res), modifier = modifier.fillMaxSize(), textAlign = TextAlign.Center)
}


@Composable
fun GetIntValueDialog(
    description: String = stringResource(id = R.string.new_item),
    @StringRes labelId: Int = R.string.item,
    onNewItemAdded : ((Int)-> Unit)? = null
) {
    val openDialog = remember { mutableStateOf(true) }

    if(openDialog.value){
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = description)
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AppTextField(
                        labelId =labelId,
                        widthPercentage = 0.9f,
                        keyboardType = KeyboardType.Number
                    ){
                        onNewItemAdded?.invoke(it.toInt())
                        openDialog.value = false
                    }
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
@Preview
fun AddProductDialog(
    onValidate : ((String?, Uri?)-> Unit)? = null
) {
    val openDialog = remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    if(openDialog.value){
        AlertDialog(
            onDismissRequest = {
                onValidate?.invoke(null, null)
                openDialog.value = false
            },
            title = {
                Text(text = "Ajouter un produit", textAlign = TextAlign.Center)
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(imageUri != null) {
                        val source = ImageDecoder.createSource(context.contentResolver,imageUri!!)
                        Log.d("AddProductDialog", "Image uri path : ${imageUri?.path}")
                        Log.d("AddProductDialog", "Image uri encoded path : ${imageUri?.encodedPath}")
                        Image(
                            bitmap = ImageDecoder.decodeBitmap(source).asImageBitmap(),
                            contentDescription =null,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.delicious_banana),
                            contentDescription = "",
                            modifier = Modifier
                                .size(150.dp)
                                .clickable {
                                    launcher.launch("image/*")
                                }
                        )
                    }
                    AppTextField(
                        labelId = R.string.item,
                        widthPercentage = 0.9f,
                        keyboardType = KeyboardType.Text
                    ){
                        name = it
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onValidate?.invoke(name, imageUri)
                        openDialog.value = false
                    }
                ) {
                    Text("Valider")
                }
            }
        )
    }
}

@Composable
@Preview
fun DeliveryDateSpinner(
    modifier : Modifier = Modifier,
    date : LocalDate?= null,
    isEnabled : Boolean = true,
    @StringRes labelId : Int = R.string.delivery_date,
    onDateSelected : ((LocalDate)-> Unit)? = null
){
    var selectedItem by remember { mutableStateOf<String>(LocalDate.now().plusDays(1).formatToShortDate())}

    // Declaring integer values for current year, month and day
    val mYear = LocalDate.now().plusDays(1).year
    val mMonth = LocalDate.now().plusDays(1).monthValue - 1
    val mDay = LocalDate.now().plusDays(1).dayOfMonth

    // Declaring a string value to store date in string format
    val mContext = LocalContext.current
    val mDatePickerDialog = DatePickerDialog(mContext,
        { _: DatePicker, _: Int, _: Int, mDayOfMonth: Int ->
            val selectedDate = "$mDayOfMonth/${mMonth+1}/$mYear"
            val dateFormatted = selectedDate.split("/").joinToString("/") { it.padStart(2, '0') }
            val localDate = LocalDate.parse(dateFormatted, DateTimeFormatter.ofPattern(SLASH_DATE_FORMAT))
            selectedItem = localDate.formatToShortDate()
            selectedItem.let { onDateSelected?.invoke(localDate) }
        }, mYear, mMonth, mDay
    )

    Box(modifier) {
        Box(
            Modifier
                .padding(top = 10.dp)
                .wrapContentSize()
                .align(Alignment.Center)
                .clip(RoundedCornerShape(20.dp))
                .background(if (isEnabled) Jaune1 else Gray1)
                .clickable(enabled = isEnabled) {
                    mDatePickerDialog.show()
                }
                .padding(10.dp)

        ) {
            Text(selectedItem)
        }
        Text(stringResource(id = labelId), modifier = Modifier.align(Alignment.TopStart), style = MaterialTheme.typography.labelSmall)
    }
}


@Composable
fun PricesRow(
    modifier: Modifier = Modifier,
    prices : List<Int> = emptyList(),
    onPriceSelected : ((Int)-> Unit)?=null
){
    // Price states // TODO Integrate dialog in price row composable ?
    var selectedPrice by remember { mutableStateOf(0) }
    var customQuantity by remember { mutableStateOf(-1) }

    // Choose custom basket price dialog
    val showCustomDialogWithResult = rememberSaveable { mutableStateOf(false) }

    if (showCustomDialogWithResult.value) {
        GetIntValueDialog {
            showCustomDialogWithResult.value = false
            customQuantity = it
            selectedPrice = it
            onPriceSelected?.invoke(selectedPrice)
        }
    }

    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        (prices +  customQuantity).forEach {
            QuantityBubble(
                size = 32.dp,
                quantity = it.toString(),
                backgroundColor = if (selectedPrice == it) Jaune1 else Gray1,
            ) { price ->
                if(price == customQuantity) showCustomDialogWithResult.value = true else selectedPrice = price
                onPriceSelected?.invoke(selectedPrice)
            }
        }
        Text("€")
    }
}

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    scale: Float = 0.6f,
    containerColor : Color = Purple80,
    icon : ImageVector = Icons.Filled.ArrowBackIos,
    onClick: (() -> Unit)?
){
    FloatingActionButton(
        onClick = { onClick?.invoke() },
        containerColor = containerColor,
        modifier = modifier.scale(scale)
    ) {
        Icon(imageVector = icon, contentDescription = "")
    }
}