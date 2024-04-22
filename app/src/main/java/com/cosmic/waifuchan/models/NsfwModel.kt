package com.cosmic.waifuchan.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.serialization.Serializable

@Serializable
data class NsfwModel (
    val files: List<String>
)


class NsfwImageListModel {
    var imageUrls: SnapshotStateList<String> = emptyList<String>().toMutableStateList()
    var selectedCategory: String = "waifu"
}