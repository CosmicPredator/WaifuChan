package com.cosmic.waifuchan

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import com.cosmic.waifuchan.helpers.ApiClient
import com.cosmic.waifuchan.helpers.RequestHandler
import com.cosmic.waifuchan.helpers.RequestHandlerImpl
import com.cosmic.waifuchan.models.NsfwImageListModel
import com.cosmic.waifuchan.models.SfwImageListModel
import com.cosmic.waifuchan.viewModels.MainViewModel
import com.cosmic.waifuchan.viewModels.NsfwViewModel
import com.cosmic.waifuchan.viewModels.SfwViewModel
import com.cosmic.waifuchan.viewModels.EnlargedImageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class MainApplication: Application(), ImageLoaderFactory {
    override fun onCreate() {
        super.onCreate()

        val appModule = module {
            viewModelOf(::MainViewModel)
            viewModelOf(::SfwViewModel)
            viewModelOf(::NsfwViewModel)
            viewModelOf(::EnlargedImageViewModel)
            factoryOf(::ApiClient)
            factoryOf(::RequestHandlerImpl) { bind<RequestHandler>() }
            singleOf(::SfwImageListModel)
            singleOf(::NsfwImageListModel)
        }

        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)
            .memoryCache(
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            )
            .diskCache(
                DiskCache.Builder()
                    .directory(this.cacheDir.resolve("waifu_pics_image_cache"))
                    .maxSizePercent(0.2)
                    .build()
            )
            .allowHardware(true)
            .build()
    }
}