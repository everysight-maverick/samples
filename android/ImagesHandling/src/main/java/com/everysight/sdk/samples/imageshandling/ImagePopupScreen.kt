package com.everysight.sdk.samples.imageshandling

import UIKit.app.Screen
import UIKit.app.data.*
import UIKit.app.resources.Font
import UIKit.app.resources.ImgSrc
import UIKit.controls.popup.PopupMessage
import UIKit.widgets.Image
import UIKit.widgets.Text
import UIKit.widgets.UIElementsGroup
import UIKit.widgets.UIElement
import kotlin.random.Random

private val random = Random(System.currentTimeMillis())
private fun Random.color() = EvsColors.fromRgb(random.nextInt(50,255),random.nextInt(50,255),random.nextInt(0,255))

/**
 * a custom control for the popup message
 * */
private class MyPopup:UIElementsGroup(){
    private var refTimestampMs = 0L
    override fun onCreate() {
        super.onCreate()
        setWidthHeight(150f,110f)
        setBackgroundColor(EvsColor.Black)

        val img = ImgSrc("apple.jpg",ImgSrc.Slot.s2)
        Image()
            .setResource(img)
            .setX(getWidth()/2-img.imageWidth/2)
            .setY(MARGIN)
            .addTo(this)
        Text()
            .setText("I'm a popup")
            .setCenter(getWidth()/2)
            .setResource(Font.StockFont.Small)
            .setY(img.imageHeight+MARGIN)
            .setForegroundColor(EvsColor.Orange)
            .addTo(this)

        showBorder(true)
    }

    /**
     * Each [UIElement] can modify its content before each draw according
     * to the application logic\view model\etc.
     * */
    override fun onBeforeDraw(timestampMs: Long) {
        super.onBeforeDraw(timestampMs)
        if(timestampMs-refTimestampMs > 500) { //change color every 0.5 sec
            setBorderStyle(random.color(), 2, 10f)
            refTimestampMs = timestampMs
        }
    }
    companion object{
        const val MARGIN = 20f
    }

}
/**
 * Show images dynamically according to the application logic
 * */
class ImagePopupScreen:Screen() {

    private var refTimestampMs = 0L
    private var popupRequired = false
    private val status = Text()
    private val img = Image()

    private fun someAppLogic(timestampMs: Long){
        popupRequired = getPopupMessage()==null && (timestampMs-refTimestampMs)>2000
    }

    private fun setStatus(text:String){
        status.setText(text).setForegroundColor(random.color())
    }
    override fun onCreate() {
        val banana = ImgSrc("banana.jpg",ImgSrc.Slot.s1)
        img
            .setResource(banana)
            .setX(getWidth()/2-banana.imageWidth/2).setY(getHeight()/2-banana.imageHeight/2) //center the image on the screen

        status
            .setResource(Font.StockFont.Small)
            .setCenter(getWidth()/2)
            .setY(img.getY()+img.getHeight()+5)//put text below the image
            .addTo(this)

        setStatus("Waiting...")

        refTimestampMs = System.currentTimeMillis()
    }


    /**
     * The [onUpdateUI] is called before each rendering flow allow to modify the screen according
     * to the application logic\view model\etc.
     * */
    override fun onUpdateUI(timestampMs: Long) {
        super.onUpdateUI(timestampMs)

        if(img.isAttachedToScreen()){
            //remove image after 3 seconds
            if(timestampMs-refTimestampMs>3000) {

                setStatus("Hiding Banana")
                img.removeSelf() // alternatively call remove(img)
                refTimestampMs = timestampMs
            }
        }else{
            //show image after 5 seconds
            if(timestampMs-refTimestampMs>5000) {
                setStatus("Showing ${img.getResource()!!.getResourceName()}")
                img.addTo(this) // alternatively call add(img)
                refTimestampMs = timestampMs
            }
        }
        someAppLogic(timestampMs)
        if(popupRequired){
            setStatus("Showing Popup")
            val align = if(random.nextFloat()>0.30) AlignV.bottom else AlignV.top
            val msg = PopupMessage(MyPopup(), align,3000){
                popupRequired = false
                setStatus("Hiding Popup")
            }
            showPopup(msg)
        }

    }
}