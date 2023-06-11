package com.everysight.sdk.samples.otahandling

import UIKit.addons.EVS_ADDON_DEVELOPER
import UIKit.addons.IEvsDeveloperService
import UIKit.services.*
import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.everysight.evskit.android.Evs


class OtaHandlingActivity : Activity(), IEvsCommunicationEvents, IEvsAppEvents {

    private val screen = OtaHandlingScreen()
    private lateinit var txtStatus: TextView
    private var otaSimEnvStarted = false

    companion object{
        private const val TAG = "OtaHandlingActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ota_handling)

        checkPerm()

        findViewById<Button>(R.id.btnInitDefault).setOnClickListener{
            initSdkDefaultOtaHandling()
            updateUIAfterInit()
        }

        findViewById<Button>(R.id.btnInitCustomTrigger).setOnClickListener{
            initSdkCustomOtaTrigger()
            updateUIAfterInit()
        }

        findViewById<Button>(R.id.btnInitCustomProgress).setOnClickListener{
            initSdkCustomOtaProgress()
            updateUIAfterInit()
        }

        txtStatus = findViewById(R.id.txtStatus)

        findViewById<Button>(R.id.btnConfigure).setOnClickListener{
            Evs.instance().showUI("configure")
        }
        findViewById<Button>(R.id.btnSettings).setOnClickListener{
            Evs.instance().showUI("settings")
        }

        findViewById<Button>(R.id.btnOtaSim).setOnClickListener{
            simOta()
        }

    }

    private fun simOta() {
        val developer = Evs.instance().getAddOn(EVS_ADDON_DEVELOPER) as IEvsDeveloperService? ?: return
        if(!otaSimEnvStarted){

            //start the OTA simulation environment (once)
            otaSimEnvStarted = true
            developer.startOtaSimulationEnvironment(
                object:IEvsOtaAvailableHandler{
                    override fun onOtaAvailable(version: Int) {
                        AlertDialog.Builder(this@OtaHandlingActivity)
                            .setTitle("New OTA Available")
                            .setMessage("Version $version")
                            .setPositiveButton("Click to Install (SIM)") { _, _ -> Evs.instance().ota().startOtaProcess()} // this is how you start to OTA process
                            .show()
                            .window?.setGravity(Gravity.CENTER)
                    }
                },
                object:IEvsOtaEventsHandler{
                    override fun onOtaFailed(errCode: OtaErrorCode, description: String) {
                        runOnUiThread{txtStatus.text = "Failed: $errCode"}
                    }

                    override fun onOtaInstallCompleted() {
                        runOnUiThread{txtStatus.text = "Glasses are up to date"}
                    }

                    override fun onOtaInstallStarted() {
                        runOnUiThread{txtStatus.text = "Installing"}
                    }

                    override fun onOtaProgress(progress: Int) {
                        runOnUiThread{txtStatus.text = "Uploading $progress%"}
                    }

                    override fun onOtaStarted(filesCount: Int) {
                        runOnUiThread{txtStatus.text = "Starting Upload ($filesCount file(s))"}
                    }

                    override fun onOtaUploadCompleted() {
                        runOnUiThread{txtStatus.text = "Upload Completed"}
                    }

                }
            )
        }


        //execute an OTA simulation (the parameter defines the simulation result. null for success)
        if(!developer.startOtaSim(null)){
            Toast.makeText(this,"startOtaSim failed",Toast.LENGTH_SHORT).show()
        }

    }

    private fun updateUIAfterInit() {
        Evs.instance().screens().addScreen(screen)

        findViewById<Button>(R.id.btnInitDefault).isEnabled = false
        findViewById<Button>(R.id.btnInitCustomTrigger).isEnabled = false
        findViewById<Button>(R.id.btnInitCustomProgress).isEnabled = false
        findViewById<Button>(R.id.btnConfigure).isEnabled = true
        findViewById<Button>(R.id.btnSettings).isEnabled = true
        findViewById<Button>(R.id.btnOtaSim).isEnabled = true
    }

    override fun onDestroy() {
        super.onDestroy()

        if(Evs.wasInitialized()) {
            Evs.instance().unregisterAppEvents(this)
            Evs.instance().comm().unregisterCommunicationEvents(this)
            Evs.instance().stop()
        }
    }

    private fun initSdkCustomOtaProgress() {
        Evs.init(this).start()
        Evs.instance().registerAppEvents(this)
        // you may implement you own UI
        Evs.instance().ota().overrideOtaProcessHandler(object:IEvsOtaEventsHandler{

            override fun onOtaFailed(errCode: OtaErrorCode, description: String) {
                runOnUiThread{txtStatus.text = "Failed: $errCode"}
            }

            override fun onOtaInstallCompleted() {
                runOnUiThread{txtStatus.text = "Glasses are up to date"}
            }

            override fun onOtaInstallStarted() {
                runOnUiThread{txtStatus.text = "Installing"}
            }

            override fun onOtaProgress(progress: Int) {
                runOnUiThread{txtStatus.text = "Uploading $progress%"}
            }

            override fun onOtaStarted(filesCount: Int) {
                runOnUiThread{txtStatus.text = "Starting Upload ($filesCount file(s))"}
            }

            override fun onOtaUploadCompleted() {
                runOnUiThread{txtStatus.text = "Upload Completed"}
            }

        })

        with(Evs.instance().comm()){
            registerCommunicationEvents(this@OtaHandlingActivity)
            if(hasConfiguredDevice()) connect()
        }

    }

    private fun initSdkCustomOtaTrigger() {
        Evs.init(this).start()
        Evs.instance().registerAppEvents(this)

        // you may implement you own UI
        Evs.instance().ota().overrideOtaAvailableHandler(object:IEvsOtaAvailableHandler{
            override fun onOtaAvailable(version: Int) {
                AlertDialog.Builder(this@OtaHandlingActivity)
                    .setTitle("New OTA Available")
                    .setMessage("Version $version")
                    .setPositiveButton("Install") { _, _ -> Evs.instance().ota().startOtaProcess()} // this is how you start to OTA process
                    .show()
                    .window?.setGravity(Gravity.CENTER)
            }

        })

        with(Evs.instance().comm()){
            registerCommunicationEvents(this@OtaHandlingActivity)
            if(hasConfiguredDevice()) connect()
        }

    }
    private fun initSdkDefaultOtaHandling() {
        Evs.init(this).start()
        Evs.instance().registerAppEvents(this)

        with(Evs.instance().comm()){
            registerCommunicationEvents(this@OtaHandlingActivity)
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

    override fun onError(errCode: AppErrorCode, description: String) {
        runOnUiThread{txtStatus.text = "Error: $errCode"}
    }
}
