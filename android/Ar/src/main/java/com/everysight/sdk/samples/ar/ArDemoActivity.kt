package com.everysight.sdk.samples.ar

import UIKit.services.AppErrorCode
import UIKit.services.IEvsAppEvents
import UIKit.services.IEvsCommunicationEvents
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.everysight.evskit.android.Evs


class ArDemoActivity : Activity() {

    private val screen = ArDemoScreen()
    private lateinit var txtStatus: TextView
    private val commEventsListener = CommEvents()
    private val appEventsListener = AppEventsListener()

    companion object{
        private const val TAG = "ArDemoActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_demo)

        checkPerm()

        initSdk()

        Evs.instance().screens().addScreen(screen)
        txtStatus = findViewById(R.id.txtStatus)

        findViewById<Button>(R.id.btnConfigure).setOnClickListener{
            Evs.instance().showUI("configure")
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener{
            Evs.instance().showUI("adjust")
        }
        findViewById<Button>(R.id.btnCalibrate).setOnClickListener{
            Evs.instance().showUI("calibrate")
        }

    }

    private fun initSdk() {
        Evs.init(this).start()
        Evs.instance().registerAppEvents(appEventsListener)
        with(Evs.instance().comm()){
            registerCommunicationEvents(commEventsListener)
            if(hasConfiguredDevice()) connect()
        }

    }

    private fun checkPerm() {
        var permissionsRequested = ArrayList<String>()
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

    private fun showMessage(msg:String){
        Log.d(TAG,msg)
        runOnUiThread {
            txtStatus.text = msg
        }
    }

    /**
     * Communication Events
     */
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
            showMessage("Failed to connect")
        }
    }

    /**
     * App Events
     */
    inner class AppEventsListener: IEvsAppEvents {

        override fun onReady() {
            showMessage("Ready!")
            Evs.instance().display().turnDisplayOn()
        }

        override fun onUnReady() {
            showMessage("UnReady!")
        }

        override fun onError(errCode: AppErrorCode, description: String) {
            showMessage("Error: $errCode")
        }

    }

}
