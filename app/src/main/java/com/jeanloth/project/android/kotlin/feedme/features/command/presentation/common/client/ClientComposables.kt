package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.AppTextField
import kotlinx.coroutines.delay

@Composable
fun GetStringValueDialog(
    description: String = stringResource(R.string.new_client),
    @StringRes labelId: Int = R.string.firstname,
    onNewClientAdded : ((String)-> Unit)? = null,
    onDismiss : (()-> Unit) ?= null
) {
    val openDialog = remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = true){
        delay(100)
        focusRequester.requestFocus()
    }

    if(openDialog.value){
        AlertDialog(
            onDismissRequest = {
                onDismiss?.invoke()
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
                        widthPercentage = 0.9f
                    ){
                        onNewClientAdded?.invoke(it)
                        openDialog.value = false
                    }
                }
            },
            confirmButton = {},
            modifier = Modifier.focusRequester(focusRequester)
        )
    }
}