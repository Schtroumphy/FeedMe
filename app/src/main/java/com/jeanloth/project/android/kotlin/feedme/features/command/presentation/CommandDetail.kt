package com.jeanloth.project.android.kotlin.feedme.features.command.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.Command
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.toNameString

@Composable
@Preview
fun CommandDetailPage(
    command : Command? = null
){
    val client = command?.client?.toNameString()
    Text("Client : $client")
}