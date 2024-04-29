package com.example.myfitfriend.presentation.dietarylogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfitfriend.data.remote.reponses.DietaryLogResponse
@Composable
fun DietaryLogItem(dietaryLogResponse: DietaryLogResponse, onClick:(DietaryLogResponse)->Unit, onDelete:(Int)->Unit ) {

    Card(backgroundColor = MaterialTheme.colors.primary.copy(.2f),
        modifier = Modifier
            .padding(.8.dp)
            .fillMaxWidth()
            .clickable { onClick(dietaryLogResponse) }

        ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column (
                Modifier
                    .padding(8.dp)
                    .weight(.8f)){
                Text(text = dietaryLogResponse.foodItem, style = MaterialTheme.typography.h6)
                Text(text = dietaryLogResponse.amountOfFood.toString())

            }
            IconButton(onClick = {onDelete(dietaryLogResponse.dietaryLogId)}) {
            Icon(imageVector = Icons.Default.Delete, contentDescription ="Delete" )
            }

        }
    }


}