package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.common.client

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.AppTextField

@Composable
fun AddClientDialog(
    onNewClientAdded : ((String)-> Unit)? = null
) {
    val openDialog = remember { mutableStateOf(true) }
    val textState = rememberSaveable { mutableStateOf("") }

    if(openDialog.value){
        AlertDialog(
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
                        onNewClientAdded?.invoke(it)
                        openDialog.value = false
                    }
                }
            },
            confirmButton = {}
        )
    }
}