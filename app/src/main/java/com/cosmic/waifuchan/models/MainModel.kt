package com.cosmic.waifuchan.models

import androidx.compose.ui.graphics.vector.ImageVector

data class NavListModel(
    val name: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)