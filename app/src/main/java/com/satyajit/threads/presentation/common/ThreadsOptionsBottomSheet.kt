package com.satyajit.threads.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Block
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.VisibilityOff
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadsOptionsBottomSheet(
    onDismiss: () -> Unit
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

                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = "Save",
                            fontWeight = FontWeight.Bold,
                        )

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Rounded.BookmarkBorder,
                                contentDescription = "Bookmark"
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
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = "Hide",
                            fontWeight = FontWeight.Bold,
                        )

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.VisibilityOff,
                                contentDescription = "hide"
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 50.dp),
                shape = RoundedCornerShape(10.dp)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = "Mute",
                            fontWeight = FontWeight.Bold,
                        )

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.NightsStay,
                                contentDescription = "Mute"
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
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = "Block",
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                        )

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Block,
                                contentDescription = "hide",
                                tint = Color.Red
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
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp)
                                .align(Alignment.CenterVertically),
                            text = "Report",
                            fontWeight = FontWeight.Bold,
                            color = Color.Red,
                        )

                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                imageVector = Icons.Outlined.Report,
                                contentDescription = "report",
                                tint = Color.Red
                            )
                        }
                    }

                }
            }

        }

    }

}