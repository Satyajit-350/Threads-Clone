package com.satyajit.threads.presentation.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipGroup(
    modifier: Modifier = Modifier,
    items: List<String>,
    defaultSelectedItemIndex:Int = 0,
    selectedItemIcon: ImageVector = Icons.Filled.Done,
    onSelectedChanged : (Int) -> Unit = {}
){
    var selectedItemIndex by remember { mutableStateOf(defaultSelectedItemIndex) }

    LazyRow(
        modifier = Modifier.padding(horizontal = 3.dp),
        userScrollEnabled = true
    ) {

        items(items.size) { index: Int ->
            FilterChip(
                modifier = modifier.padding(horizontal = 3.dp),
                selected = items[selectedItemIndex] == items[index],
                onClick = {
                    selectedItemIndex = index
                    onSelectedChanged(index)
                },
                label = { Text(items[index]) },
                leadingIcon = {

                    if (items[selectedItemIndex] == items[index]){
                        Icon(
                            imageVector = selectedItemIcon,
                            contentDescription = "Localized Description",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }

                }
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewFilterChipGroup() {
    FilterChipGroup(items = listOf("All", "Follows", "Replies", "Mentions", "Quotes", "Reposts", "Verified"),
        onSelectedChanged = {
        })
}