package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.imePadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.jeanloth.project.android.kotlin.feedme.R

enum class FieldType(val label : String, val icon : ImageVector, val keyboardType : KeyboardType, val isOptional : Boolean = false, val maxChar: Int = 10){
    FIRST_NAME("Nom", Icons.Rounded.Person, KeyboardType.Text),
    LAST_NAME("Prénom", Icons.Outlined.Person, KeyboardType.Text),
    PHONE_NUMBER("Numéro de téléphone", Icons.Rounded.Phone, KeyboardType.Phone)
}

@Composable
@Preview
fun AddClientPage(context : Context){

    ProvideWindowInsets(consumeWindowInsets = true) {
        Column(
            Modifier
                .fillMaxSize()
                .navigationBarsWithImePadding()
                .padding(dimensionResource(id = R.dimen.big_margin))
                .verticalScroll(rememberScrollState())
                .imePadding() // padding for the bottom for the IME
                ,
            verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            FieldType.values().forEachIndexed { index, field ->
                AppTextField(field.label, field.icon, field.keyboardType, field.isOptional, isLastField = index == FieldType.values().size - 1)
            }
            Button("Valider") { Toast.makeText(context, "Clic sur valider", Toast.LENGTH_SHORT) }
        }
    }
}

@Composable
@Preview
fun AppTextField(
    title : String = "Champs de texte",
    icon: ImageVector = Icons.Default.Person,
    keyboardType: KeyboardType,
    isOptional: Boolean = false,
    maxChar : Int = 10,
    isLastField : Boolean = false
){
    var text by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.padding(horizontal = 15.dp)){
        Column(verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.small_margin))) {
            //External label
            Text(
                text = "$title ${if(isOptional) "(optionnel)" else ""}",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = DarkGray
            )

            TextField(
                value = text,
                onValueChange = {
                    if (it.length <= maxChar) text = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp, horizontal = 5.dp),
                shape = RoundedCornerShape(25.dp),
                trailingIcon = {
                    Icon(icon, "", tint = White)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = LightGray,
                    focusedIndicatorColor =  Color.Transparent, //hide the indicator
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = false,
                    keyboardType = keyboardType,
                    imeAction = if(isLastField) ImeAction.Done else ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onDone = {  focusManager.clearFocus() }
                )
            )
            //counter message
            Text(
                text = "${text.length} / $maxChar",
                textAlign = TextAlign.End,
                color = DarkGray,
                style = MaterialTheme.typography.labelSmall, //use the caption text style
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}