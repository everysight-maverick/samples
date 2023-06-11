package com.everysight.sdk.samples.animations

import UIKit.app.Animator
import UIKit.app.Screen
import UIKit.app.data.Align
import UIKit.app.data.EvsColor
import UIKit.app.resources.Font
import UIKit.widgets.Text
import UIKit.widgets.UIElement
import kotlin.math.round

class AnimationsScreen:Screen() {

    private val text = Text()
    private val simpleCtrl1 = SimpleControl()
    private val simpleCtrl2 = SimpleControl()

    override fun onCreate() {
        text.setText("Animations").setResource(Font.StockFont.Medium).setTextAlign(Align.center)
        text.setX(getWidth()/2).setY(getHeight()/2).setForegroundColor(EvsColor.Green.rgba)
        add(text)

        simpleCtrl1.setWidth(30f).setHeight(30f).setX(30f).setY(30f)
        simpleCtrl2.setWidth(70f).setHeight(30f).setX(230f).setY(130f)

        add(simpleCtrl1)
        add(simpleCtrl2)
    }

    override fun onResume() {
        super.onResume()
        //NOTE: text and image ui elements can be animated only with translation (no scale, no rotate)
        text.animator().duration(3000).translateTo(10f,10f).reverse().start()
        simpleCtrl1.animator()
            .scaleX(2f).scaleY(0.5f)
            .duration(5000)
            .translateTo(130f,30f)
            .bounce()
            .start()

        simpleCtrl2.animator()
            .scale(1.5f)
            .rotate(45f)
            .repeat()
            .duration(1000)
            .register(object : Animator.IEvsAnimationListener{
                var cnt = 0
                override fun animationEnded(uiElement: UIElement) {
                }

                override fun animationLooped(uiElement: UIElement) {
                    cnt++
                }

                override fun animationStarted(uiElement: UIElement) {
                }

                override fun animationStep(uiElement: UIElement, progress: Double) {
                    text.setText("Loop $cnt ${round(progress * 100)}%")
                }

            })
            .start()
    }
}