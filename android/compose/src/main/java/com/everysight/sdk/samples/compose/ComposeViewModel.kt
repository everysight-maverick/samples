package com.everysight.sdk.samples.compose

import UIKit.services.AppErrorCode
import UIKit.services.IEvsAppEvents
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.everysight.evskit.android.Evs

class ComposeViewModel(application: Application): AndroidViewModel(application) {
    var wasEvsSdkInitialized by mutableStateOf(Evs.wasInitialized())
        private set

    private val screen = ComposeScreen()
    private val appEvents = object : IEvsAppEvents {
        override fun onReady() {
            Log.d(TAG,"onReady")
            Evs.instance().display().turnDisplayOn()
        }
        override fun onUnReady() {
            Log.d(TAG,"onUnReady")
        }
        override fun onError(errCode: AppErrorCode, description: String) {
            Log.e(TAG,"onError $errCode $description")
        }
    }

    init {
        Log.d(TAG,"init")
        initSdk()
        Evs.instance().screens().addScreen(screen)
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG,"onCleared")
        Evs.instance().unregisterAppEvents(appEvents)
        Evs.instance().stop()
        wasEvsSdkInitialized = Evs.wasInitialized()
    }

    private fun initSdk() {
        Log.d(TAG,"initSdk")
        val context = getApplication<Application>()
        Evs.init(context).start()
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(appEvents)
        with(Evs.instance().comm()) {
            if (hasConfiguredDevice()) connect()
        }
        wasEvsSdkInitialized = Evs.wasInitialized()
    }


    companion object{
        private const val TAG = "ComposeViewModel"
    }

}