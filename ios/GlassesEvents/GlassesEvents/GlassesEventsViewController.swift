//Copyright (c) 2020 Everysight LTD

import UIKit
import EvsKit
import NativeEvsKit

class GlassesEventsViewController: UIViewController {

    @IBOutlet weak var statusLbl: UILabel!
    
    private let screen = GlassesEventsScreen()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initSdk()
        Evs.instance().screens().addScreen(screen: screen)
    }

    deinit {
        Evs.instance().stop()
        Evs.instance().unregisterAppEvents(listener: self)
        Evs.instance().comm().unregisterCommunicationEvents(listener: self)
        Evs.instance().glasses().unregisterGlassesEvents(listener: self)
        Evs.instance().unregisterAppEvents(listener: self)
    }

    private func initSdk() {
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(listener: self)
        Evs.instance().glasses().registerGlassesEvents(listener: self)
        Evs.instance().comm().registerCommunicationEvents(listener: self)
        Evs.instance().start()
        if(Evs.instance().comm().hasConfiguredDevice()) {
            Evs.instance().comm().connect()
        }
    }

    func showMessage(_ msg:String) {
        DispatchQueue.main.async {
            self.statusLbl.text = msg
        }
    }
    
    @IBAction func onConfiguredPressed(_ sender: Any) {
        statusLbl.text = "Disconnected"
        Evs.instance().showUI(name: "configure")
    }
    
    @IBAction func onSettingsPressed(_ sender: Any) {
        Evs.instance().showUI(name: "adjust")
    }
    
}

extension GlassesEventsViewController : IEvsAppEvents {
    func onBeginAuth(serial: String, fwVersion: Int32) {
        // here you may select the correct api key file according to the fwVersion & serial
        // and call Evs.instance().setApiKeyName(file name) to set it

    }
    
    func onBeforeRendering(timestamp: Int64) {
     
    }
    
    func onReady() {
        Evs.instance().display().turnDisplayOn()
    }
    
    func onUnReady() {
        
    }
    
    func onError(errCode: AppErrorCode, description: String) {
        showMessage("ֿ\(description)")
    }
}

extension GlassesEventsViewController : IEvsGlassesEvents {
    func onPowerButton(action: PowerButtonAction) {
        showMessage("Power Button \(action.name)")
    }
    
    func onBrightnessChangeRequested(value: Int16) {
        showMessage("Brightness = \(value)")
    }
    
    func onChargerStateChanged(isChargerConnected: Bool) {
        showMessage("Charger Connected = \(isChargerConnected)")
    }
    
    func onProximity(state: ProximityActionType) {
        showMessage("Glasses Proximity state = \(state)")
    }
    
    func onBatteryChanged(percentage: Int32) {
        showMessage("Battery Level \(percentage)%")
    }
    
    func onDisplayState(isDisplayOn: Bool) {
        showMessage("Screen is on = \(isDisplayOn)")
    }
    
    func onTouch(touch: TouchDirection) {
        showMessage("Touch \(touch)")
    }
    
}
extension GlassesEventsViewController : IEvsCommunicationEvents {
    func onFailedToConnect() {
        showMessage("Failed to connect")
    }
    
    func onAdapterStateChanged(isEnabled: Bool) {
        showMessage("Adapter enabled=\(isEnabled)")
    }
    
    func onConnected() {
        showMessage("\(Evs.instance().comm().getDeviceName() ?? "") is Connected")
    }
    
    func onConnecting() {
        showMessage( "Connecting \(Evs.instance().comm().getDeviceName() ?? "")")
    }
    
    func onDisconnected() {
        showMessage("Disconnected")
    }
}
