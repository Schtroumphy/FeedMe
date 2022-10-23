package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common

import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
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
import com.jeanloth.project.android.kotlin.feedme.core.extensions.clearFocusOnKeyboardDismiss
import com.jeanloth.project.android.kotlin.feedme.core.theme.Jaune1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1
import kotlinx.coroutines.delay

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
fun QuantityBubble(
    quantity: Int = 20,
    backgroundColor: Color = White,
    padding: Dp = 2.dp,
    onClick: ((Int) -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current

    Box(
        Modifier
            .wrapContentSize()
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable {
                focusManager.clearFocus()
                onClick?.invoke(quantity)
            }
            .padding(padding)
    ) {
        Text(if(quantity  > 0) quantity.toString() else "?", modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun PriceBox(modifier: Modifier = Modifier, color: Color = Orange1, price: String = "15€") {
    Box(
        modifier
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text = price, textAlign = TextAlign.Center)
    }
}

@Composable
fun IconBox(
    modifier: Modifier = Modifier,
    color: Color = Orange1,
    icon: ImageVector = Icons.Filled.Edit
) {
    Box(
        modifier
            .padding(5.dp)
            .clip(CircleShape)
            .background(color)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(imageVector = icon, tint = White, contentDescription = "icon button")
    }
}


@Composable
@Preview
fun AppTextField(
    modifier: Modifier = Modifier,
    widthPercentage: Float = 0.6f,
    @StringRes labelId: Int = R.string.label,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: ((String) -> Unit)? = null,
    onTextEntered: ((String) -> Unit)? = null
) {
    var text by remember { mutableStateOf("") }
    val textFieldRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box {
        Box(
            modifier
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
        Text(stringResource(id = labelId), modifier = Modifier.align(Alignment.TopStart))
    }

    LaunchedEffect(true){
        delay(100)
        textFieldRequester.requestFocus()
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
fun YesNoDialog(
    @StringRes title: Int = R.string.save_state,
    @StringRes question: Int = R.string.save_current_command,
    @StringRes yesLabel: Int = R.string.yes,
    onYesClicked : (()-> Unit)? = null,
    @StringRes noLabel: Int = R.string.no,
    onNoClicked : (()-> Unit)? = null,
) {
    AlertDialog(
        onDismissRequest = {
            onNoClicked?.invoke()
        },
        title = {
            Text(text = stringResource(id = title), style = MaterialTheme.typography.titleMedium)
        },
        text = {
            Text(text = stringResource(id = question), style = MaterialTheme.typography.labelMedium)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onYesClicked?.invoke()
                }
            ) {
                Text(stringResource(id = yesLabel))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onNoClicked?.invoke()
                }
            ) {
                Text(stringResource(id = noLabel))
            }
        }
    )
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