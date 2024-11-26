package com.everysight.sdk.samples.ar

import UIKit.app.data.EvsColor
import UIKit.app.data.TouchDirection
import UIKit.ar.elements.ArModel
import UIKit.ar.elements.ArWindow
import UIKit.ar.screens.ArScreen
import UIKit.ar.utilities.ArFactory
import UIKit.ar.utilities.Quaternion
import UIKit.widgets.Text
import android.util.Log
import com.everysight.evskit.android.Evs
import kotlin.random.Random

class ArDemoScreen: ArScreen() {
    private var tapText: HudText? = null
    private val worldpos = floatArrayOf(0f, 0f, 0f)
    private val cubes = ArrayList<ArDrawInfo>()
    private val texts = ArrayList<HudText>()
    private val random = Random(System.currentTimeMillis())
    private var quatOK = false

    override fun onCreate() {
        super.onCreate()
        Evs.instance().sensors().enableTouch(true)
    }

    override fun onUpdateUI(timestampMs: Long) {
        super.onUpdateUI(timestampMs)
        if (tapText == null) {
            tapText = HudText("LOADING...")
            tapText!!.setX(getWidth() / 2 - tapText!!.getWidth() / 2)
            tapText!!.setY(getHeight() / 2 - tapText!!.getHeight() / 2)
            add(tapText!!)
        }
        if (!quatOK && quat.isValid && !quat.isIdentity) {
            quatOK = true
            tapText!!.setText("TAP TO PLACE ELEMENTS")
        }
        cubes.forEach {
            it.angle += it.direction * 0.8f
            it.model.rotate(it.angle, 0f, 45f)
        }
        tapText!!.setEyePosition(quat)
    }

    override fun onTouch(touchDirection: TouchDirection): Boolean {
        super.onTouch(touchDirection)
        if (touchDirection == TouchDirection.tap) {
            if (quat.isValid && !quat.isIdentity) {
                Log.d(TAG, "TAP")
                tapText?.removeSelf()

                if (cubes.size > 0 && random.nextFloat() > 0.65) {
                    val t = HudText(responses[random.nextInt(0, responses.lastIndex)])
                    t.setEyePosition(quat)
                    add(t)
                    texts.add(t)
                    return true
                }

                val isCube = random.nextFloat() > 0.3f
                val c = if (isCube) ArFactory.makeCube() else ArFactory.makeArrow()
                val scale = if (isCube) 0.25f else 0.30f
                c.scale(scale, scale, scale)
                c.rotate(0f, 0f, 45f)

                eye2world(0f, 0f, -20f * random.nextDouble(0.3, 1.2).toFloat(), quat, worldpos)
                c.translate(worldpos[0], worldpos[1], worldpos[2])
                add(c)
                cubes.add(ArDrawInfo(c, 0f, if (random.nextBoolean()) 1 else -1))

            }
        }

        return true
    }

    /**
     * Transforms eye coordinates to [world coordinates](https://everysight.github.io/maverick_docs/sdk-engine/sensors/#inertial-sensors) using a quaternion.
     *
     * @param xEye The x-coordinate of the eye
     * @param yEye The y-coordinate of the eye
     * @param zEye The z-coordinate of the eye
     * @param q The quaternion representing the rotation
     * @param res The resulting world 3x1 vector
     */
    fun eye2world(xEye: Float, yEye: Float, zEye: Float, q: Quaternion, res: FloatArray) {
        q.rotateVector(xEye, yEye, zEye, res)
    }

    companion object {
        private val responses = arrayListOf("COOL!", "WOW!", "NICE ONE", "WELL DONE", "PERFECT", "AMAZING")
        const val TAG = "ArDemoScreen"
    }

    class HudText(private var txt: String): ArWindow() {
        private val text = Text()
        override fun onCreate() {
            super.onCreate()
            setText(txt)
            setBorderStyle(EvsColor.Green.rgba, 2, 10f)
            showBorder(true)
            add(text)
            setBackgroundColor(EvsColor.Black)
        }

        fun setText(txt: String) {
            this.txt = txt
            text.setText(txt)

            setWidthHeight(text.getWidth() * 1.2f, text.getHeight() * 1.2f)

            text.setCenter(getWidth() / 2)
            text.setY(getHeight() / 2 - text.getHeight() / 2 + 2)
            text.setForegroundColor(EvsColor.Yellow)

        }
    }

    data class ArDrawInfo(val model: ArModel, var angle: Float = 0f, var direction: Int = 1)

}