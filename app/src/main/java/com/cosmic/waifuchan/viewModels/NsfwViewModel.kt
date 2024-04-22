package com.cosmic.waifuchan.viewModels

import androidx.lifecycle.ViewModel
import com.cosmic.waifuchan.helpers.RequestHandler
import com.cosmic.waifuchan.models.NsfwImageListModel

class NsfwViewModel(
    private val imageListModel: NsfwImageListModel,
    private val requestHandler: RequestHandler
) : ViewModel() {



    suspend fun loadImages(category: String) {
        val data = requestHandler.getNsfwPics(exclude = imageListModel.imageUrls, category = category.lowercase())
        imageListModel.imageUrls.addAll(data.files)
    }
}