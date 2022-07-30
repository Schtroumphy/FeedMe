package com.jeanloth.project.android.kotlin.feedme.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

enum class PresenceState(val icon: ImageVector, val selectedColor: Color){
    UNVALIDATED(Icons.Filled.Close, Color.Red),
    VALIDATED(Icons.Filled.Check, Color.Green)
}
@Composable
@Preview
fun CrewItem(){
    Column(Modifier.padding(15.dp)) {
        CrewHeader()
        Spacer(Modifier.height(10.dp))
        PresenceToValidateColumn()
    }
}

@Composable @Preview
fun MultiToggleButton(
    currentSelection: PresenceState = PresenceState.VALIDATED,
    toggleStates: Array<PresenceState> = PresenceState.values(),
    onToggleChange: ((PresenceState) -> Unit)?= null
) {


    Row(modifier = Modifier
        .height(IntrinsicSize.Min)
    ) {
        toggleStates.forEachIndexed { index, toggleState ->
            val selectedTint = toggleState.selectedColor
            val isSelected = currentSelection == toggleState
            val backgroundTint = if (isSelected) selectedTint else Color.White
            val textColor = if (isSelected) Color.White else toggleState.selectedColor


            val leftCorner = if(index == 0) 8.dp else 0.dp
            val rightCorner = if(index == 0) 0.dp else 8.dp

            if (index != 0) {
                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.1.dp)
                )
            }
            val corners = RoundedCornerShape(
                topStart = leftCorner,
                bottomStart = leftCorner,
                topEnd = rightCorner,
                bottomEnd = rightCorner
            )

            Box(
                modifier = Modifier
                    .clip(corners)
                    .border(1.dp, Color.LightGray, shape = corners)
                    .background(backgroundTint)
                    .padding(vertical = 6.dp, horizontal = 8.dp)
                    .toggleable(
                        value = isSelected,
                        enabled = true,
                        onValueChange = { selected ->
                            if (selected) {
                                onToggleChange?.invoke(toggleState)
                            }
                        })
            ) {
                //Text(toggleState.toCapital(), color = textColor, modifier = Modifier.padding(4.dp))
                Icon(imageVector = toggleState.icon, contentDescription = toggleState.name, tint = textColor)
            }

        }
    }
}

@Composable
@Preview
fun PresenceToValidateColumn(){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(start = 20.dp)){
        Text("Présence à valider", color = Color.Magenta, fontWeight = FontWeight.SemiBold)
        PresenceItem()
        PresenceItem()
        PresenceItem()
    }
}

@Composable@Preview
fun PresenceItem(){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        MultiToggleButton()
        Text("Paris Montparnasse Hall 1 & 2 > Marseille Saint-Charles",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CrewConnexionState(){
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        Box(modifier = Modifier
            .clip(CircleShape)
            .size(8.dp)
            .background(Color.Green))
        Text("Connecté : David Baron")
    }
}

@Composable
fun CrewHeader(){
    Column (verticalArrangement = Arrangement.spacedBy(5.dp)){
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            Icon(imageVector = Icons.Filled.Person, contentDescription = "", modifier = Modifier.size(18.dp))
            Text("Agent X", fontStyle = FontStyle.Normal, fontWeight = FontWeight.Bold)
        }
        CrewConnexionState()
    }
}