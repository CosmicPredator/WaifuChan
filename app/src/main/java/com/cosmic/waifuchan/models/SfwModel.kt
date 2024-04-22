package com.cosmic.waifuchan.models

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import kotlinx.serialization.Serializable

@Serializable
data class SfwModel (
    val files: List<String>
)


class SfwImageListModel {
    var imageUrls: SnapshotStateList<String> = emptyList<String>().toMutableStateList()
    var selectedCategory: String = "waifu"
}