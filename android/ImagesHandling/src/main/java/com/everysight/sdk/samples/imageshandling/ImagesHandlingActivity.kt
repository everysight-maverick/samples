package com.everysight.sdk.samples.imageshandling

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


class ImagesHandlingActivity : Activity(), IEvsCommunicationEvents, IEvsAppEvents {

    private  val screens = arrayListOf(ImagesHandlingScreen(),ImagesBatchScreen(),ImagePopupScreen())
    private var nextScreenIndex = 0

    private lateinit var txtStatus: TextView
    companion object{
        private const val TAG = "ImagesHandlingActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_images_handling)

        checkPerm()

        initSdk()

        showNextScreen()
        txtStatus = findViewById(R.id.txtStatus)

        findViewById<Button>(R.id.btnConfigure).setOnClickListener{
            Evs.instance().showUI("configure")
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener{
            Evs.instance().showUI("adjust")
        }

        findViewById<Button>(R.id.btnNextScreen).setOnClickListener{
            showNextScreen()
        }

    }

    private fun showNextScreen() {
        Evs.instance().screens().removeTopmostScreen()
        Evs.instance().screens().addScreen(screens[nextScreenIndex])
        nextScreenIndex = (nextScreenIndex+1)%screens.size
    }

    override fun onDestroy() {
        super.onDestroy()
        Evs.instance().unregisterAppEvents(this)
        Evs.instance().comm().unregisterCommunicationEvents(this)
        Evs.instance().stop()
    }

    private fun initSdk() {
        Evs.init(this).start()
        Evs.instance().registerAppEvents(this)
        with(Evs.instance().comm()){
            registerCommunicationEvents(this@ImagesHandlingActivity)
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

    override fun onAdapterStateChanged(isEnabled: Boolean) {
        runOnUiThread{txtStatus.text = "Adapter enabled=$isEnabled"}
    }

    override fun onConnected() {
        runOnUiThread{txtStatus.text = "${Evs.instance().comm().getDeviceName()} is Connected"}
    }

    override fun onConnecting() {
        runOnUiThread{txtStatus.text = "Connecting ${Evs.instance().comm().getDeviceName()}"}
    }

    override fun onDisconnected() {
        runOnUiThread{txtStatus.text = "Disconnected"}
    }

    override fun onFailedToConnect() {
        runOnUiThread{txtStatus.text = "${Evs.instance().comm().getDeviceName()} Failed to connect"}
    }

    override fun onReady() {
        Evs.instance().display().turnDisplayOn()
    }

    override fun onUnReady() {

    }

    override fun onError(errCode: AppErrorCode, description: String) {
        runOnUiThread{txtStatus.text = "Error: $errCode"}
    }
}
