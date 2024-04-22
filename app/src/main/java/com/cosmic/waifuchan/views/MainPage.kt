package com.cosmic.waifuchan.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cosmic.waifuchan.Routes
import com.cosmic.waifuchan.models.ImageCategory
import com.cosmic.waifuchan.models.ThemeMode
import com.cosmic.waifuchan.viewModels.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(
    rootNavController: NavHostController,
    viewModel: MainViewModel = koinViewModel(),
    onThemeUpdated: (ThemeMode) -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val navController = rememberNavController()

    val snackBarHost = remember {
        SnackbarHostState()
    }

    Scaffold(
        bottomBar = { BottomNavView(viewModel = viewModel, navController) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { ScrollToTopButton(viewModel = viewModel) },
        snackbarHost = { SnackbarHost(hostState = snackBarHost) },
        topBar = {
            TopAppBar(
                title = { Text(text = viewModel.titleBarText) },
                scrollBehavior = scrollBehavior,
                actions = {
                    if (viewModel.selectedIndex <= 1) {
                        Box {
                            IconButton(onClick = { viewModel.isDropDownExpanded = !viewModel.isDropDownExpanded }) {
                                Icon(
                                    imageVector = Icons.Rounded.FilterAlt,
                                    contentDescription = "Filter"
                                )
                            }
                            FilterDialog(
                                isExpanded = viewModel.isDropDownExpanded,
                                itemSelected = { value ->
                                    if (viewModel.selectedIndex == 0) {
                                        viewModel.selectedSfwCategory = value
                                    } else {
                                        viewModel.selectedNsfwCategory = value
                                    }
                                },
                                items = if (viewModel.selectedIndex == 0) ImageCategory.sfwCategories
                                    else ImageCategory.nsfwCategories,
                                onDismissRequest = {
                                    viewModel.isDropDownExpanded = false
                                },
                                viewModel = viewModel,
                                isNsfw = viewModel.selectedIndex == 1
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.SFW_PAGE,
            modifier = Modifier.padding(paddingValues),
            enterTransition = {
                fadeIn(animationSpec = tween(400))
            },
            exitTransition =  {
                fadeOut(animationSpec = tween(400))
            }
        ) {
            composable(Routes.SFW_PAGE) {
                SFWPage(mainViewModel = viewModel, snackbarHost = snackBarHost, navController = rootNavController) }
            composable(Routes.NSFW_PAGE) {
                NSFWPage(mainViewModel = viewModel, navController = rootNavController) }
            composable(Routes.SETTINGS) { SettingsPage(onThemeUpdated = onThemeUpdated) }
        }
    }
}

@Composable
fun BottomNavView(viewModel: MainViewModel, navController: NavHostController) {
    NavigationBar {
        viewModel.navBarList.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = viewModel.selectedIndex == index,
                onClick = {
                    viewModel.selectedIndex = index
                    when (index) {
                        0 -> {
                            navController.navigate(Routes.SFW_PAGE)
                            viewModel.titleBarText = "SFW Waifu"
                        }

                        1 -> {
                            navController.navigate(Routes.NSFW_PAGE)
                            viewModel.titleBarText = "NSFW Waifu"
                        }

                        2 -> {
                            navController.navigate(Routes.SETTINGS)
                            viewModel.titleBarText = "Settings"
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (viewModel.selectedIndex != index) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.name
                    )
                },
                label = {
                    Text(text = item.name)
                }
            )
        }
    }
}

@Composable
fun ScrollToTopButton(viewModel: MainViewModel) {
    AnimatedVisibility(
        visible = viewModel.isFABVisible,
        enter = slideInVertically(animationSpec = tween(durationMillis = 200)) { fullHeight ->
            -fullHeight / 3
        } + fadeIn(animationSpec = tween(durationMillis = 100)),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 200)) { fullHeight: Int ->
            fullHeight
        } + fadeOut(animationSpec = tween(durationMillis = 100))
    ) {
        FloatingActionButton(
            onClick = { viewModel.isScrollToTop = true }
        ) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp,
                contentDescription = "Go Up"
            )
        }
    }
}