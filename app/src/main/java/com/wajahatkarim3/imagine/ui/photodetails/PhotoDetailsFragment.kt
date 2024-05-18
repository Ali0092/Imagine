/*
* Copyright 2021 Wajahat Karim (https://wajahatkarim.com)
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.wajahatkarim3.imagine.ui.photodetails

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.drawToBitmap
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.wajahatkarim3.imagine.base.BaseFragment
import com.wajahatkarim3.imagine.databinding.PhotoDetailsFragmentBinding
import com.wajahatkarim3.imagine.model.PhotoModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoDetailsFragment : BaseFragment<PhotoDetailsFragmentBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> PhotoDetailsFragmentBinding
        get() = PhotoDetailsFragmentBinding::inflate

    private val viewModel: PhotoDetailsViewModel by viewModels()
    private val wallPaperManager:WallpaperManager  by lazy { WallpaperManager.getInstance(requireContext().applicationContext) }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val photo = arguments?.getParcelable<PhotoModel>("photo")
        if (photo == null) {
            findNavController().popBackStack()
            return
        }

        setupViews()
        initObservations()

        viewModel.initPhotoModel(photo)
    }

    private fun setupViews() {

        bi.setHomeButton.setOnClickListener {
            wallPaperManager.setBitmap(getBitmapFromView())
        }
        bi.setLockButton.setOnClickListener {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                wallPaperManager.setBitmap(getBitmapFromView(), null, true, WallpaperManager.FLAG_LOCK)
            }
        }
        bi.setBothButton.setOnClickListener {
            wallPaperManager.setBitmap(getBitmapFromView())
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                wallPaperManager.setBitmap(getBitmapFromView(), null, true, WallpaperManager.FLAG_LOCK)
            }
        }
        bi.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initObservations() {
        viewModel.photoModelLiveData.observe(viewLifecycleOwner) { photo ->
            bi.photoView.load(photo.urls.full){
                allowHardware(false)
                bitmapConfig(Bitmap.Config.ARGB_8888)
            }
        }
    }

    private fun getBitmapFromView(): Bitmap {
        bi.photoView.isDrawingCacheEnabled = true
        bi.photoView.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(bi.photoView.drawingCache)
        bi.photoView.isDrawingCacheEnabled = false
        return bitmap
    }

}
