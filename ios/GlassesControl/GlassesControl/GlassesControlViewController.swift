//Copyright (c) 2020 Everysight LTD

import UIKit
import EvsKit
import NativeEvsKit

class GlassesControlViewController: UIViewController {
    
    @IBOutlet weak var statusLbl: UILabel!
    private let screen = GlassesControlScreen()
    override func viewDidLoad() {
        super.viewDidLoad()
        initSdk()
        Evs.instance().screens().addScreen(screen: screen)
    }
    
    deinit {
        Evs.instance().unregisterAppEvents(listener: self)
        Evs.instance().comm().unregisterCommunicationEvents(listener: self)
        Evs.instance().glasses().unregisterGlassesEvents(listener: self)
        Evs.instance().stop()
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
    
    @IBAction func onConfigureGlassesPressed(_ sender: Any) {
        statusLbl.text = "Disconnected"
        Evs.instance().showUI(name: "configure")
    }
    
    @IBAction func onSettingsPressed(_ sender: Any) {
        Evs.instance().showUI(name: "adjust")
    }
    
    @IBAction func onScreenOnPressed(_ sender: Any) {
        Evs.instance().display().turnDisplayOn()
    }
    
    @IBAction func onScreenOffPressed(_ sender: Any) {
        Evs.instance().display().turnDisplayOff()
    }
    
    @IBAction func onMoveLeftPressed(_ sender: Any) {
        Evs.instance().screens().getRenderingCenterX()
        let center = Evs.instance().screens().getRenderingCenterX() - 10
        Evs.instance().screens().setRenderingCenterX(centerX: center)
        showMessage("Screen center X=\(Evs.instance().screens().getRenderingCenterX())")
    }
    
    @IBAction func onMoveRightPressed(_ sender: Any) {
        let centerX = Evs.instance().screens().getRenderingCenterX() + 10
        Evs.instance().screens().setRenderingCenterX(centerX: centerX)
        showMessage("Screen center X=\(Evs.instance().screens().getRenderingCenterX())")
    }
    
    @IBAction func onLessBrightnessPressed(_ sender: Any) {
        let brightness = Evs.instance().display().getBrightness() - 10
        Evs.instance().display().setBrightness(value: brightness)
        showMessage("Brightness \(Evs.instance().display().getBrightness())")
    }
    
    @IBAction func onMoreBrightnessPressed(_ sender: Any) {
        let brightness = Evs.instance().display().getBrightness() + 10
        Evs.instance().display().setBrightness(value: brightness)
        showMessage("Brightness \(Evs.instance().display().getBrightness())")
    }
    
}

extension GlassesControlViewController: IEvsGlassesEvents {
    func onTouch(touch: TouchDirection) {
        showMessage("Touch Direction \(touch)")
    }
    
    func onPowerButton(action: PowerButtonAction) {
        var isPressedMsg = ""
        switch action {
        case .pressed: isPressedMsg = "Pressed"; break
        case .released: isPressedMsg = "Released"; break
        case .longPressed: isPressedMsg = "Long press"; break
        default: isPressedMsg = "Unknown"; break
        }
        showMessage("Power Button \(isPressedMsg)")
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
        showMessage("Battery Level \(percentage)")
    }
    
    func onDisplayState(isDisplayOn: Bool) {
        showMessage("Screen is on = \(isDisplayOn)")
    }
}

extension GlassesControlViewController : IEvsCommunicationEvents {
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

extension GlassesControlViewController : IEvsAppEvents {
    func onBeginAuth(serial: String, fwVersion: Int32) {
        // here you may select the correct api key file according to the fwVersion & serial
        // and call Evs.instance().setApiKeyName(file name) to set it

    }
    
    func onBeforeRendering(timestamp: Int64) {
     
    }
    
    func onReady() {
        Evs.instance().display().turnDisplayOn()
        Evs.instance().sensors().enableTouch(enable: true)
    }
    
    func onUnReady() {
        
    }
    
    func onError(errCode: AppErrorCode, description: String) {
        showMessage("ֿ\(description)")
    }
}

