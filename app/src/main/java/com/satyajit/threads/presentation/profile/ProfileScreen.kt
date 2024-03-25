package com.satyajit.threads.presentation.profile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Segment
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.satyajit.threads.R
import com.satyajit.threads.modals.User
import com.satyajit.threads.navigation.Routes
import com.satyajit.threads.presentation.profile.common.OverlappingImages
import com.satyajit.threads.presentation.profile.common.SuggestedProfileItems
import com.satyajit.threads.presentation.profile.tab_screens.RepliesScreen
import com.satyajit.threads.presentation.profile.tab_screens.RepostScreen
import com.satyajit.threads.presentation.profile.tab_screens.ThreadsScreen
import com.satyajit.threads.utils.SharedPref
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navHostController: NavHostController
) {

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val scrollState = rememberLazyListState()

    val context = LocalContext.current

    var isCollapsed by remember {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            ProfileScreenTopAppBar(
                onPrivacyClick = {
                    navHostController.navigate(Routes.Privacy.route)
                },
                onSettingsClick = {
                    navHostController.navigate(Routes.Settings.route)
                },
                onInstagramClick = {
                    context.launchCustomTabs(url = "https://instagram.com", useIncognito = false)
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            AnimatedVisibility(
                visible = scrollState.firstVisibleItemScrollOffset == 0,
                enter = expandVertically(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = shrinkVertically(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1f)
                        ) {

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = SharedPref.getUserName(context).ifEmpty { "Thread User" },
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Row(
                                modifier = Modifier.wrapContentWidth()
                            ) {

                                Text(
                                    modifier = Modifier.padding(1.dp),
                                    text = SharedPref.getUserName(context)
                                        .ifEmpty { "Thread User" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 5.dp)
                                        .wrapContentWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.LightGray.copy(0.2f))
                                ) {

                                    Text(
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp, vertical = 2.dp
                                        ),
                                        text = "threads.net",
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )

                                }

                            }

                            Spacer(modifier = Modifier.height(5.dp))

                            if(SharedPref.getBio(context)!=""){
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 5.dp, vertical = 5.dp)
                                        .wrapContentHeight(),
                                    text = SharedPref.getBio(context),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row(
                                modifier = Modifier.wrapContentWidth()
                            ) {
                                // OverlappingImages - custom composable
                                OverlappingImages(
                                    small_circle_size = 10.dp,
                                    larger_circle_size = 15.dp,
                                    translation_val_x = -10f,
                                    translation_val_y = 10f
                                )

                                Spacer(modifier = Modifier.width(1.dp))

                                Text(
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp, vertical = 2.dp
                                    ),
                                    text = "23 followers", //TODO
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Image(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            painter = if (SharedPref.getImageUrl(context) != "")
                                rememberAsyncImagePainter(model = SharedPref.getImageUrl(context))
                            else
                                painterResource(id = R.drawable.default_profile_img),
                            contentDescription = "profile_pic",
                            contentScale = ContentScale.Crop
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    ) {

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = {
                                navHostController.navigate(Routes.EditProfile.route)
                            },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Edit Profile",
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        OutlinedButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            onClick = { /*TODO*/ },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Share Profile",
                                fontWeight = FontWeight.Bold,
                            )
                        }

                    }

                    AnimatedVisibility(
                        visible = !isCollapsed,
                        enter = expandVertically(),
                        exit = shrinkVertically()
                    ) {

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                        ) {

                            Row {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    text = "Suggested for you",
                                    fontSize = 13.sp
                                )

                                val icon = Icons.Default.Close

                                Icon(
                                    modifier = Modifier
                                        .clickable {
                                            isCollapsed = !isCollapsed
                                        },
                                    imageVector = icon,
                                    contentDescription = "toggle icons"
                                )
                            }

                            SuggestedPeoplesList()

                        }

                    }

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .padding(top = 5.dp)
                    )
                }
            }
            TabBarLayout(
                navHostController = navHostController,
                scrollState = scrollState
            )
        }

    }

}

fun Context.launchCustomTabs(url: String, useIncognito: Boolean?) {
    CustomTabsIntent.Builder().build().apply {
        if (useIncognito == true) {
            intent.putExtra(
                "com.google.android.apps.chrome.EXTRA_OPEN_NEW_INCOGNITO_TAB",
                true
            )
        }
    }
        .launchUrl(this, Uri.parse(url))
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabBarLayout(
    navHostController: NavHostController,
    scrollState: LazyListState
) {

    val tabData = getTabList()
    val pagerState = rememberPagerState(pageCount = tabData.size)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TabLayout(
            tabData, pagerState
        )
        TabContent(
            pagerState = pagerState,
            navController = navHostController,
            scrollState = scrollState
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(tabData: List<String>, pagerState: PagerState) {

    val scope = rememberCoroutineScope()

    TabRow(
        selectedTabIndex = pagerState.currentPage,
        divider = {
            Spacer(modifier = Modifier.height(5.dp))
        },
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .pagerTabIndicatorOffset(
                        pagerState = pagerState,
                        tabPositions = tabPositions
                    )
                    .padding(horizontal = 3.dp),
                height = 3.dp,
                color = Color.Black
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        backgroundColor = MaterialTheme.colors.onPrimary,
    ) {
        tabData.forEachIndexed { index, data ->
            Tab(selected = pagerState.currentPage == index, onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
                text = {
                    Text(text = data, fontSize = 12.sp)
                }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(
    pagerState: PagerState,
    navController: NavHostController,
    scrollState: LazyListState
) {

    HorizontalPager(state = pagerState) { index ->
        when (index) {
            0 -> {
                ThreadsScreen(
                    navHostController = navController,
                    scrollState = scrollState
                )
            }

            1 -> {
                RepliesScreen()
            }

            2 -> {
                RepostScreen()
            }
        }
    }
}

private fun getTabList(): List<String> {
    return listOf(
        "Threads",
        "Replies",
        "Repost"
    )
}

@Composable
private fun SuggestedPeoplesList() {

    val profile_list = listOf(
        User("SatyajitBiswal", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
        User("Satyajit", "_satya_biswal_", "ajabf", "", ""),
    )

    LazyRow(
        state = rememberLazyListState(),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(count = profile_list.size) {
            profile_list[it]?.let { user ->
                SuggestedProfileItems(
                    user = user
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreenTopAppBar(
    onPrivacyClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onInstagramClick: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior? = null,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .animateContentSize(animationSpec = tween(durationMillis = 200))
            .padding(horizontal = 5.dp, vertical = 15.dp),
        title = {},
        navigationIcon = {
            IconButton(onClick = {
                onPrivacyClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "privacy"
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onInstagramClick()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.instagram_logo),
                    contentDescription = "instagram",
                    tint = Color.Black
                )
            }
            IconButton(onClick = {
                onSettingsClick()
            }) {
                Icon(
                    imageVector = Icons.Default.Segment,
                    contentDescription = "settings",
                    tint = Color.Black
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
//    val navHostController: NavHostController?=null
//    ProfileScreen(navHostController!!)
}