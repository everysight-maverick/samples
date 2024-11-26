package com.everysight.sdk.samples.inertialsensors

import UIKit.app.Screen
import UIKit.app.data.CalibrationStatus
import UIKit.app.data.ScreenRenderRate
import UIKit.app.data.YprData
import UIKit.services.IEvsYprSensorsEvents
import com.everysight.evskit.android.Evs


class InertialSensorsScreen:Screen() {
    val compass = Compass()
    override fun onCreate() {

        Evs.instance().display().turnDisplayOn()
        setScreenRenderRate(ScreenRenderRate.fast)
        compass.setX(getWidth()/2-compass.getWidth()/2)
        compass.setY(getHeight()/2-compass.getHeight()/2+40)

        add(compass)
    }

    override fun onResume() {
        super.onResume()
        Evs.instance().sensors().registerYprSensorsEvents(fusedEvent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Evs.instance().sensors().unregisterYprSensorsEvents(fusedEvent)
    }

    private val fusedEvent = object : IEvsYprSensorsEvents {
        override fun onYpr(
            timestampMs: Long,
            yprData: YprData,
            calibrationStatus: CalibrationStatus
        ) {
            //not corrected with magnetic declination
            val angle = yprData.yaw
            compass.setWorldAngle(angle,calibrationStatus)
        }
    }
}