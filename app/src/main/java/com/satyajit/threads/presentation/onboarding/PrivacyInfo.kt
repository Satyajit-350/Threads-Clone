package com.satyajit.threads.presentation.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.onboarding.common.OnboardingHeaderItem

@Composable
fun PrivacyInfo(
    navHostController: NavHostController
) {

    var selected by remember { mutableStateOf(SelectableOption.Card1) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Icon(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(10.dp)
                .clickable {
                    navHostController.popBackStack()
                },
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .clickable {
                    navHostController.navigate(Routes.FollowInfo.route)
                },
            imageVector = Icons.Default.ArrowForward,
            contentDescription = null
        )

        OnboardingHeaderItem(
            modifier = Modifier.padding(top = 40.dp),
            title = "Privacy",
            subTitle = "Your privacy can be different on Threads and Instagram. Learn More",
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.Center)
        ) {

            SelectableItem(
                selected = selected == SelectableOption.Card1,
                title = "Public Profile",
                subtitle = "Anyone on ir off the Threads can see. share and interact with your content",
                icon = Icons.Outlined.Language,
                onClick = {
                    selected = SelectableOption.Card1
                }
            )

            SelectableItem(
                selected = selected == SelectableOption.Card2,
                title = "Private Profile",
                subtitle = "Only your approved followers can see, share and interact with your content.",
                icon = Icons.Outlined.Language,
                onClick = {
                    selected = SelectableOption.Card2
                }
            )

        }

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .align(Alignment.BottomCenter),
            onClick = {
                navHostController.navigate(Routes.FollowInfo.route)
            },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF020202))
        ) {
            Text(
                modifier = Modifier.padding(vertical = 5.dp),
                text = "Continue",
                fontSize = 16.sp
            )
        }

    }

}

@Composable
fun SelectableItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    title: String,
    titleColor: Color =
        if (selected) Color.Black
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    subtitle: String,
    subtitleColor: Color =
        if (selected) MaterialTheme.colorScheme.onSurface
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    borderWidth: Dp = 1.dp,
    borderColor: Color =
        if (selected) Color.Black
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    icon: ImageVector,
    iconColor: Color =
        if (selected) Color.Black
        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
    onClick: () -> Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        border = BorderStroke(borderWidth, borderColor),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(
                        color = titleColor
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                text = subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.Light,
                style = TextStyle(
                    color = subtitleColor
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }
}

enum class SelectableOption {
    Card1,
    Card2,
}

//@Preview(showBackground = true)
//@Composable
//private fun privacyinfoPreview() {
//    PrivacyInfo()
//}