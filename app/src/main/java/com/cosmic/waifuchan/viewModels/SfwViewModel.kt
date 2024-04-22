package com.cosmic.waifuchan.viewModels

import androidx.lifecycle.ViewModel
import com.cosmic.waifuchan.helpers.RequestHandler
import com.cosmic.waifuchan.models.SfwImageListModel

class SfwViewModel(
    private val imageListModel: SfwImageListModel,
    private val requestHandler: RequestHandler
) : ViewModel() {

    suspend fun loadImages(category: String) {
        val data = requestHandler.getSfwPics(exclude = imageListModel.imageUrls, category = category.lowercase())
        imageListModel.imageUrls.addAll(data.files)
    }
}