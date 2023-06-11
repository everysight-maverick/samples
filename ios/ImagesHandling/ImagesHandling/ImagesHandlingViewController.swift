//Copyright (c) 2020 Everysight LTD

import UIKit
import EvsKit
import NativeEvsKit

class ImagesHandlingViewController: UIViewController {
    
    @IBOutlet weak var statusLbl: UILabel!
    private let screen = ImagesHandlingScreen()
    private var screenBatch : ImagesBatchScreen?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        initSdk()
        Evs.instance().screens().addScreen(screen: screen)
    }
    
    deinit {
        Evs.instance().stop()
        Evs.instance().unregisterAppEvents(listener: self)
        Evs.instance().comm().unregisterCommunicationEvents(listener: self)
    }
    
    private func initSdk() {
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(listener: self)
        Evs.instance().comm().registerCommunicationEvents(listener: self)
        Evs.instance().start()
        if(Evs.instance().comm().hasConfiguredDevice()) {
            Evs.instance().comm().connect()
        }
    }
    
    @IBAction func btnClicked(_ sender: Any) {
        if(screenBatch==nil){
            screenBatch = ImagesBatchScreen()
            //we add screen to the screen stack to be displayed
            Evs.instance().screens().addScreen(screen: screenBatch!)
        }
        else{
            //we remove the screen from the screen stack. the previous screen will
            //become the topmost screen so it will be displayed on the glasses
            Evs.instance().screens().removeScreen(screen: screenBatch!)
            screenBatch = nil
        }
    }
    
    
    func showMessage(_ msg:String) {
        DispatchQueue.main.async {
            self.statusLbl.text = msg
        }
    }
    
    @IBAction func onConfigurePressed(_ sender: Any) {
        statusLbl.text = "Disconnected"
        Evs.instance().showUI(name: "configure")
    }
    
    @IBAction func onSettingsPressed(_ sender: Any) {
        Evs.instance().showUI(name: "settings")
    }
    
}

extension ImagesHandlingViewController : IEvsCommunicationEvents {
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

extension ImagesHandlingViewController : IEvsAppEvents {
    
    func onBeginAuth(serial: String, fwVersion: Int32) {
        // here you may select the correct api key file according to the fwVersion & serial
        // and call Evs.instance().setApiKeyName(file name) to set it

    }
    
    func onBeforeRendering(timestamp: Int64) {
        
    }
    
    func onReady() {
        Evs.instance().display().turnDisplayOn()
    }
    
    func onError(errCode: AppErrorCode, description: String) {
        showMessage("ֿ\(description)")
    }
}
