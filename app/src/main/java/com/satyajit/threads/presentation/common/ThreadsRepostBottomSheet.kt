package com.satyajit.threads.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.satyajit.threads.R
import com.satyajit.threads.presentation.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadsRepostBottomSheet(
    modifier: Modifier = Modifier,
    isReposted: Boolean = false,
    onDismiss: () -> Unit,
    onRepostClicked: () -> Unit,
    onRepostQuotesClicked: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                onRepostClicked()
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = if(isReposted) "Remove" else "Repost",
                            fontWeight = FontWeight.Bold,
                            color = if(isReposted) Color.Red else Color.DarkGray
                        )

                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_repost),
                                contentDescription = "Repost",
                                tint = if(isReposted) Color.Red else Color.DarkGray
                            )
                        }
                    }

                    Divider(
                        modifier = Modifier
                            .height(1.dp)
                            .padding(horizontal = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .clickable {
                                onRepostQuotesClicked()
                            }
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = "Quote",
                            fontWeight = FontWeight.Bold,
                        )

                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_quote),
                                contentDescription = "Quote"
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}