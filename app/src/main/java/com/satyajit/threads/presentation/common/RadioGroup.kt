package com.satyajit.threads.presentation.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    radioOptions: List<Any>
) {

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[1] ) }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        radioOptions.forEach { text ->
            Row(
                modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = {
                            onOptionSelected(text)
                        }
                    )

            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                        .weight(1f),
                    text = text.toString(),
                    color = Color.Gray
                )
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RadioGroupPreview() {
    RadioGroup(radioOptions = listOf("A", "B", "C"))
}