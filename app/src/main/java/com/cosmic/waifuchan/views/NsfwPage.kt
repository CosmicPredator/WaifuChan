package com.cosmic.waifuchan.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cosmic.waifuchan.models.NsfwImageListModel
import com.cosmic.waifuchan.viewModels.MainViewModel
import com.cosmic.waifuchan.viewModels.NsfwViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun NSFWPage(
    viewModel: NsfwViewModel = koinViewModel(),
    imageListModel: NsfwImageListModel = koinInject(),
    mainViewModel: MainViewModel,
    navController: NavController
) {
    var loaded by rememberSaveable {
        mutableStateOf(false)
    }

    val listState = rememberLazyStaggeredGridState().also {
        if (!it.canScrollForward) {
            LaunchedEffect (Unit) {
                viewModel.loadImages(mainViewModel.selectedNsfwCategory)
            }
        }
        mainViewModel.isFABVisible = it.firstVisibleItemScrollOffset > 50
    }

    LaunchedEffect(mainViewModel.isScrollToTop) {
        if (mainViewModel.isScrollToTop) {
            listState.animateScrollToItem(0)
            mainViewModel.isScrollToTop = false
        }
    }

    LaunchedEffect(mainViewModel.selectedNsfwCategory) {
        if (imageListModel.selectedCategory != mainViewModel.selectedNsfwCategory.lowercase() ||
            imageListModel.imageUrls.isEmpty()) {
            loaded = false
            imageListModel.selectedCategory = mainViewModel.selectedNsfwCategory.lowercase()
            imageListModel.imageUrls.clear()
            viewModel.loadImages(mainViewModel.selectedNsfwCategory)
        }
        loaded = true
    }

    if (loaded) {
        LazyVerticalStaggeredGrid(
            state = listState,
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 5.dp,
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            flingBehavior = ScrollableDefaults.flingBehavior(),
            userScrollEnabled = true,
            content = {
                imageListModel.imageUrls.forEach {
                    item {
                        ImageCard(
                            imageUrl = it,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .clickable {
                                    navController.navigate("/imageview?imageUrl=${it}")
                                }
                        )
                    }
                }
            }
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp)
            )
        }
    }
}