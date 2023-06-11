package com.everysight.sdk.samples.imageshandling

import UIKit.app.Screen
import UIKit.app.data.*
import UIKit.app.resources.ImgSrc
import UIKit.widgets.Image

class ImagesHandlingScreen:Screen() {

    override fun onCreate() {
        val banana = Image()
            .setResource(ImgSrc("banana.jpg",ImgSrc.Slot.s1))
            .setX(100f).setY(130f)
        add(banana)

        val appleSrc = ImgSrc("apple.jpg",ImgSrc.Slot.s2)

        val apple1 = Image().setResource(appleSrc).setX(200f).setY(110f)
        add(apple1)

        // Since apple1 and apple2 uses the same image src 'apple.jpg', we should use the same image source ('appleSrc')
        // so it will be uploaded to the glasses just once (same slot)
        val apple2 = Image()
            .setResource(appleSrc)
            .setX(200f).setY(150f)
        add(apple2)

    }
}