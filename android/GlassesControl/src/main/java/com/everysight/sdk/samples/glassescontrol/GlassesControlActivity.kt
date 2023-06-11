package com.everysight.sdk.samples.glassescontrol

import UIKit.app.data.PowerButtonAction
import UIKit.services.AppErrorCode
import UIKit.services.IEvsAppEvents
import UIKit.services.IEvsCommunicationEvents
import UIKit.services.IEvsGlassesEvents
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.everysight.evskit.android.Evs


class GlassesControlActivity : Activity(), IEvsAppEvents {

    private val screen = GlassesControlScreen()
    private lateinit var txtStatus: TextView
    private val commEventsListener = CommEvents()
    private val glassesEventsListener = GlassesEventsListener()

    companion object{
        private const val TAG = "GlassesControlActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_glasses_control)

        checkPerm()

        initSdk()

        Evs.instance().screens().addScreen(screen)
        txtStatus = findViewById(R.id.txtStatus)


    }

    override fun onDestroy() {
        super.onDestroy()
        Evs.instance().unregisterAppEvents(this)
        Evs.instance().comm().unregisterCommunicationEvents(commEventsListener)
        Evs.instance().glasses().unregisterGlassesEvents(glassesEventsListener)
        Evs.instance().stop()
    }

    private fun initSdk() {
        Evs.init(this).start()
        Evs.instance().registerAppEvents(this)
        Evs.instance().glasses().registerGlassesEvents(glassesEventsListener)
        with(Evs.instance().comm()){
            registerCommunicationEvents(commEventsListener)
            if(hasConfiguredDevice()) connect()
        }

    }

    private fun checkPerm() {
        var permissionsRequested = ArrayList<String>()
        permissionsRequested.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionsRequested.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionsRequested.add(Manifest.permission.BLUETOOTH_CONNECT)
        }else{
            permissionsRequested.add(Manifest.permission.BLUETOOTH)
        }
        permissionsRequested = validatePermissions(permissionsRequested)
        if (permissionsRequested.size > 0) {

            Log.d(TAG,"validating permissions")
            val req = permissionsRequested.toTypedArray()
            requestPermissions(req, 666)
        }
    }
    private fun validatePermissions(permissionsRequested: ArrayList<String>): ArrayList<String> {
        val permissionsToRequest = ArrayList<String>()
        for (permission in permissionsRequested) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission)
            }
        }
        return permissionsToRequest
    }

    inner class CommEvents:IEvsCommunicationEvents {
        override fun onAdapterStateChanged(isEnabled: Boolean) {
            showMessage("Adapter enabled=$isEnabled")
        }

        override fun onConnected() {
            showMessage("${Evs.instance().comm().getDeviceName()} is Connected")
        }

        override fun onConnecting() {
            showMessage( "Connecting ${Evs.instance().comm().getDeviceName()}")
        }

        override fun onDisconnected() {
            showMessage("Disconnected")
        }

        override fun onFailedToConnect() {
            showMessage( "Failed connecting ${Evs.instance().comm().getDeviceName()}")
        }
    }

    inner class GlassesEventsListener:IEvsGlassesEvents{
        override fun onBatteryChanged(percentage: Int) {
            showMessage("Battery Level $percentage%")
        }

        override fun onBrightnessChangeRequested(value: Short) {
            showMessage("Brightness $value%")
        }

        override fun onChargerStateChanged(isChargerConnected: Boolean) {
            showMessage("Charger Connected = $isChargerConnected")
        }

        override fun onDisplayState(isDisplayOn: Boolean) {
            showMessage("Screen is on = $isDisplayOn")
        }

        override fun onPowerButton(action: PowerButtonAction) {
            showMessage("Power Button $action")
        }

    }

    fun configureGlasses(view: View) {Evs.instance().showUI("configure")}
    fun showSettings(view: View) {Evs.instance().showUI("settings")}
    fun screenOn(view: View) {Evs.instance().display().turnDisplayOn()}
    fun screenOff(view: View) {Evs.instance().display().turnDisplayOff()}
    fun screenLeft(view: View) {
        with(Evs.instance().screens()) {
            setRenderingCenterX(getRenderingCenterX() - 10)
            showMessage("Screen Center X=${getRenderingCenterX()}")
        }
    }
    fun screenRight(view: View) {
        with(Evs.instance().screens()){
            setRenderingCenterX(getRenderingCenterX()+10)
            showMessage("Screen Center X=${getRenderingCenterX()}")
        }
    }


    fun brightnessMinus(view: View) {
        with(Evs.instance().display()){
            setBrightness((getBrightness()-10).toShort())
            showMessage("Brightness ${getBrightness()}")
        }
    }

    fun brightnessPlus(view: View) {
        with(Evs.instance().display()){
            setBrightness((getBrightness()+10).toShort())
            showMessage("Brightness ${getBrightness()}")
        }
    }

    private fun showMessage(msg:String){
        runOnUiThread {
            txtStatus.text = msg
            screen.text.setText(msg)
        }
    }

    override fun onReady() {
        Evs.instance().display().turnDisplayOn()
        Evs.instance().sensors().enableTouch(true)
    }

    override fun onError(errCode: AppErrorCode, description: String) {
        showMessage("Error: $errCode")
    }
}
