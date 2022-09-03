package com.jeanloth.project.android.kotlin.feedme.features.command.presentation.client

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jeanloth.project.android.kotlin.feedme.R
import com.jeanloth.project.android.kotlin.feedme.core.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Orange1
import com.jeanloth.project.android.kotlin.feedme.core.theme.Red
import com.jeanloth.project.android.kotlin.feedme.features.command.domain.models.AppClient
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.IconBox
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.NoDataText
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.PersonName


@Composable
fun ClientListPage(
    viewModel: ClientVM,
    onClientRemoved : ((AppClient) -> Unit)? = null
){
    val uiState by viewModel.clientUiState.collectAsState()

    when(uiState){
        is ClientUiState.EmptyList -> NoDataText(res = R.string.no_client)
        is ClientUiState.NoEmptyList -> ClientList((uiState as ClientUiState.NoEmptyList).clients, onClientRemoved)
    }

}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun ClientList(clients : List<AppClient>, onClientRemoved : ((AppClient) -> Unit)? = null){
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        items(clients,{client:AppClient->client.idClient}){ client ->
            val dismissState = rememberDismissState()

            if (dismissState.isDismissed(DismissDirection.EndToStart)) {
                onClientRemoved?.invoke(client)
            }
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier
                    .padding(vertical = 1.dp),
                directions = setOf(
                    DismissDirection.EndToStart
                ),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                },
                background = {
                    val color by animateColorAsState(
                        when (dismissState.targetValue) {
                            DismissValue.Default -> Color.White
                            else -> Red.copy(alpha = 0.5f)
                        }
                    )
                    val alignment = Alignment.CenterEnd
                    val icon = Icons.Default.Delete

                    val scale by animateFloatAsState(
                        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                    )

                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = Dp(20f)),
                        contentAlignment = alignment
                    ) {
                        Icon(
                            icon,
                            contentDescription = "Delete Icon",
                            modifier = Modifier.scale(scale),
                            tint = Color.White
                        )
                    }
                },
                dismissContent = {
                    ClientRow(client)
                }
            )
        }
    }
}

@Composable
fun ClientRow(client: AppClient, color : Color = Orange1){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ClientNameBox(name= client.firstname, modifier = Modifier.weight(1f), color = color)
        IconBox(icon = Icons.Outlined.Phone, color = color)
        IconBox(icon = Icons.Outlined.Mail, color = color)
    }
}

@Composable
@Preview
fun ClientNameBox(modifier: Modifier = Modifier, name: String = "Mael DUMAT", color : Color = Orange1){
    ConstraintLayout(
        modifier = modifier
    ) {
        val (clientBox, editBox) = createRefs()

        // Client box
        Box(
            Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Gray1)
                .border(
                    width = 2.dp,
                    color = color,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 15.dp, vertical = 10.dp)
                .constrainAs(clientBox) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
        ){
            PersonName(name = name)
        }
        // Icon box
        IconBox(
            color = color,
            modifier = Modifier
                .padding(end = 30.dp)
                .constrainAs(editBox) {
                    end.linkTo(clientBox.end)
                    bottom.linkTo(clientBox.bottom)
                }
        )
    }

}