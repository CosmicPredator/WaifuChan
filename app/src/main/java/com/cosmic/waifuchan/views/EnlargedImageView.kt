package com.cosmic.waifuchan.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.cosmic.waifuchan.viewModels.EnlargedImageViewModel
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import org.koin.androidx.compose.koinViewModel
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnlargedImageView(
    imageUrl: String?,
    navController: NavHostController,
    viewModel: EnlargedImageViewModel = koinViewModel()
) {

    val context = LocalContext.current

    val zoomState = rememberZoomState()
    Box (
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = "Enlarged Image",
            loading = { LoadingIndicator() },
            modifier = Modifier.zoomable(zoomState)
        )
        TopAppBar(
            title = {
                if (imageUrl != null) {
                    Text(text = imageUrl.split("/").last())
                }
            },
            modifier = Modifier
                .align(Alignment.TopStart)
                .alpha(0.7f),
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back Arrow"
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    if (imageUrl != null) {
                        viewModel.downloadImage(imageUrl, context)
                    }
                }) {
                    Icon(imageVector = Icons.Rounded.Download, contentDescription = "Download")
                }
            }
        )
    }
}

@Composable
fun LoadingIndicator() {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(50.dp)
                .height(50.dp)
        )
    }
}