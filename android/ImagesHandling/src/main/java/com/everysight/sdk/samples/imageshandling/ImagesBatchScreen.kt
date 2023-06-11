package com.everysight.sdk.samples.imageshandling

import UIKit.app.Screen
import UIKit.app.data.*
import UIKit.app.resources.Font
import UIKit.app.resources.ImgSrc
import UIKit.app.resources.UIResource
import UIKit.widgets.Image
import UIKit.widgets.Text
import UIKit.widgets.UIResourceElement

class ImagesBatchScreen:Screen() {

    private val text = Text()

    override fun onCreate() {
        super.onCreate()
        text.setResource(Font.StockFont.Small).setCenter(getWidth()/2).setY(getHeight()/3)
        add(text)

        //your can upload custom fonts as well via the Text UI element
        requestResourcesUpload(screenImages)

    }

    override fun onResourcesUploadStart() {
        super.onResourcesUploadStart()
        text.setVisibility(true)
        text.setText("Loading...")
    }
    override fun onResourceUploadResult(resource: UIResource) {
        text.setText("${resource.getResourceName()} was loaded")
    }

    override fun onResourcesUploadEnd() {
        super.onResourcesUploadEnd()
        text.setVisibility(false)
        screenImages.forEachIndexed{index,imgSrc->
            add(Image().setResource(imgSrc).setX(index*50f).setY(getHeight()/2))
        }

    }

    companion object{
        private val screenImages = arrayListOf<ImgSrc>()
        init {
            screenImages.add(ImgSrc("blast.jpg",ImgSrc.Slot.s1))
            screenImages.add(ImgSrc("heart.jpg",ImgSrc.Slot.s2))
            screenImages.add(ImgSrc("ship.jpg",ImgSrc.Slot.s3))
            screenImages.add(ImgSrc("watermelon.jpg",ImgSrc.Slot.s4))
            screenImages.add(ImgSrc("orange.jpg",ImgSrc.Slot.s5))
        }
    }

}