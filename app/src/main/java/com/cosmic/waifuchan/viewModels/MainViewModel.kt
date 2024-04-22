package com.cosmic.waifuchan.viewModels

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.cosmic.waifuchan.models.NavListModel

@OptIn(SavedStateHandleSaveableApi::class)
class MainViewModel(savedStateHandle: SavedStateHandle): ViewModel() {

    var navBarList by savedStateHandle.saveable {
        mutableStateOf(emptyList<NavListModel>())
    }

    var isScrollToTop by savedStateHandle.saveable {
        mutableStateOf(false)
    }

    var isFABVisible by savedStateHandle.saveable {
        mutableStateOf(true)
    }

    var selectedIndex by savedStateHandle.saveable {
        mutableIntStateOf(0)
    }

    var titleBarText by savedStateHandle.saveable {
        mutableStateOf("SFW Waifu")
    }

    var selectedSfwCategory by savedStateHandle.saveable {
        mutableStateOf("waifu")
    }

    var selectedNsfwCategory by savedStateHandle.saveable {
        mutableStateOf("waifu")
    }

    var isDropDownExpanded by savedStateHandle.saveable {
        mutableStateOf(false)
    }

    init {
        updateNavBarList()
    }

    private fun updateNavBarList() {
        val navList = listOf(
            NavListModel("SFW", Icons.Outlined.FavoriteBorder, Icons.Filled.Favorite),
            NavListModel("NSFW", Icons.Outlined.Warning, Icons.Filled.Warning),
            NavListModel("Settings", Icons.Outlined.Settings, Icons.Filled.Settings)
        )
        navBarList = navList
    }
}