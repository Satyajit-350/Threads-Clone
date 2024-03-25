package com.satyajit.threads.presentation.onboarding.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomSearchBar(
    modifier: Modifier = Modifier,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    active: Boolean,
    onActiveChanged: (Boolean) -> Unit
) {
    androidx.compose.material3.SearchBar(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        query = searchText,
        onQueryChange = { onSearchTextChanged(it) },
        onSearch = { onActiveChanged(false) },
        active = active,
        onActiveChange = { onActiveChanged(it) },
        shape = RoundedCornerShape(8.dp),
        placeholder = {
            Text(text = "Search")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "search Icon"
            )
        },
        trailingIcon = {
            if (searchText.isNotEmpty()) {
                Icon(
                    modifier = Modifier.clickable { onSearchTextChanged("") },
                    imageVector = Icons.Default.Close,
                    contentDescription = "close icon"
                )
            }
        }
    ){}
}