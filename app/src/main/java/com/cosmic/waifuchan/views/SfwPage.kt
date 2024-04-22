package com.cosmic.waifuchan.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.cosmic.waifuchan.Routes
import com.cosmic.waifuchan.models.SfwImageListModel
import com.cosmic.waifuchan.viewModels.MainViewModel
import com.cosmic.waifuchan.viewModels.SfwViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


@Composable
fun SFWPage(
    viewModel: SfwViewModel = koinViewModel(),
    imageListModel: SfwImageListModel = koinInject(),
    mainViewModel: MainViewModel,
    snackbarHost: SnackbarHostState,
    navController: NavHostController
) {

    var loaded by rememberSaveable {
        mutableStateOf(false)
    }

    val listState = rememberLazyStaggeredGridState().also {
        if (!it.canScrollForward) {
            LaunchedEffect (Unit) {
                viewModel.loadImages(mainViewModel.selectedSfwCategory)
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

    LaunchedEffect(mainViewModel.selectedSfwCategory) {
        if (imageListModel.selectedCategory != mainViewModel.selectedSfwCategory.lowercase() ||
            imageListModel.imageUrls.isEmpty()) {
            loaded = false
            imageListModel.selectedCategory = mainViewModel.selectedSfwCategory.lowercase()
            imageListModel.imageUrls.clear()
            viewModel.loadImages(mainViewModel.selectedSfwCategory)
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

@Composable
fun ImageCard(imageUrl: String, modifier: Modifier) {

    SubcomposeAsyncImage(
        model = imageUrl,
        filterQuality = FilterQuality.High,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .defaultMinSize(minHeight = 200.dp),
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.onSecondary)
                    .clip(RoundedCornerShape(10.dp))
            )
        }
    )
}