package com.satyajit.threads.presentation.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

@Composable
fun BasicTextFiledWithHint(
    modifier: Modifier = Modifier,
    hint: String,
    value: String,
    onValueChange: (String) -> Unit,
    isEnabled: (Boolean) -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {

        if (value.isEmpty()) {
            Text(text = hint, color = Color.LightGray)
            isEnabled(false)
        }

        BasicTextField(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle.Default.copy(Color.Black)
        )

    }
}