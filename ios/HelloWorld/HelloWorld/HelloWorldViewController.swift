//Copyright (c) 2020 Everysight LTD

import UIKit
import EvsKit
import NativeEvsKit

class HelloWorldViewController: UIViewController {
    
    var helloWorldScreen = HelloWorldScreen()
    @IBOutlet weak var statusLbl: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(listener: self)
        Evs.instance().comm().registerCommunicationEvents(listener: self)
        Evs.instance().start()
        if(Evs.instance().comm().hasConfiguredDevice()){
            Evs.instance().comm().connect()
        }
        Evs.instance().screens().addScreen(screen: helloWorldScreen)
    }
    
    deinit {
        Evs.instance().stop()
        Evs.instance().unregisterAppEvents(listener: self)
        Evs.instance().comm().unregisterCommunicationEvents(listener: self)
    }

    @IBAction func onConfiguredPressed(_ sender: Any) {
        statusLbl.text = "Disconnected"
        Evs.instance().showUI(name: "configure")
    }
    
    @IBAction func onSettingsPressed(_ sender: Any) {
        Evs.instance().showUI(name: "adjust")
    }
}

extension HelloWorldViewController : IEvsCommunicationEvents {
    
    func showMessage(_ msg:String) {
        DispatchQueue.main.async {
            self.statusLbl.text = msg
        }
    }
    
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
        showMessage("Connecting \(Evs.instance().comm().getDeviceName() ?? "")")
    }
    
    func onDisconnected() {
        showMessage("Disconnected")
    }
    
}

extension HelloWorldViewController : IEvsAppEvents {
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
