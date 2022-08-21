package com.jeanloth.project.android.kotlin.feedme.presentation.ui.client

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Gray1
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Orange1
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Vert1
import com.jeanloth.project.android.kotlin.feedme.presentation.theme.Violet1
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.IconBox
import com.jeanloth.project.android.kotlin.feedme.presentation.ui.PersonName

@Composable
//@Preview
fun ClientListPage(){

    Column(
        Modifier.fillMaxWidth().padding(top = 10.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ClientRow()
        ClientRow(Violet1)
        ClientRow(Vert1)
        ClientRow()
    }
}

@Composable
@Preview
fun ClientRow(color : Color = Orange1){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        ClientNameBox(modifier = Modifier.weight(1f), color = color)
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
            PersonName(name = "Albert MANCHON")
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
