//Copyright (c) 2020 Everysight LTD

import UIKit
import EvsKit
import NativeEvsKit

class OtaHandlingViewController: UIViewController {
    
    @IBOutlet weak var statusLbl: UILabel!
    
    private let screen = OtaHandlingScreen()
    
    @IBOutlet weak var btnInitDefault: UIButton!
    
    @IBOutlet weak var btnInitCustomTrigger: UIButton!
    
    @IBOutlet weak var btnInitCustomProgress: UIButton!
    
    @IBOutlet weak var btnSettings: UIButton!
    
    @IBOutlet weak var btnConfigure: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        btnInitDefault.isEnabled = true
        btnInitDefault.isEnabled = true
        btnInitCustomProgress.isEnabled = true
        btnSettings.isEnabled = false
        btnConfigure.isEnabled = false
    }
    
    deinit {
        Evs.instance().stop()
        Evs.instance().unregisterAppEvents(listener: self)
        Evs.instance().comm().unregisterCommunicationEvents(listener: self)
    }
    
    private func updateUIAfterInit() {
        Evs.instance().screens().addScreen(screen: screen)
       
        btnInitDefault.isEnabled = false
        btnInitCustomTrigger.isEnabled = false
        btnInitCustomProgress.isEnabled = false
        btnSettings.isEnabled = true
        btnConfigure.isEnabled = true
    }
    
    func showMessage(_ msg:String) {
        DispatchQueue.main.async {
            self.statusLbl.text = msg
        }
    }
    
    @IBAction func initSdkDefaultOtaHandling(_ sender: Any) {
        updateUIAfterInit()
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(listener: self)
        Evs.instance().comm().registerCommunicationEvents(listener: self)
        Evs.instance().start()
        if(Evs.instance().comm().hasConfiguredDevice()) {
            Evs.instance().comm().connect()
        }
    }
    
    @IBAction func initSdkCustomOtaTrigger(_ sender: Any) {
        updateUIAfterInit()
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(listener: self)
        Evs.instance().ota().overrideOtaAvailableHandler(handler: self)
        Evs.instance().comm().registerCommunicationEvents(listener: self)
        Evs.instance().start()
        if(Evs.instance().comm().hasConfiguredDevice()) {
            Evs.instance().comm().connect()
        }
    }
    
    @IBAction func initSdkCustomOtaProgress(_ sender: Any) {
        updateUIAfterInit()
        Evs.startDefaultLogger()//optional
        Evs.instance().registerAppEvents(listener: self)
        Evs.instance().ota().overrideOtaProcessHandler(handler: self)
        Evs.instance().comm().registerCommunicationEvents(listener: self)
        Evs.instance().start()
        if(Evs.instance().comm().hasConfiguredDevice()) {
            Evs.instance().comm().connect()
        }
    }
    var txt = ""
    @IBAction func startOtaSim(_ sender: Any) {
        txt = " - (SIM MODE)"
        if let developerService = Evs.instance().getAddOn(addOnName: AddonNamesKt.EVS_ADDON_DEVELOPER) as? IEvsDeveloperService {
            developerService.startOtaSimulationEnvironment(otaAvailableHandler: self, otaProcessHandler: self)
            developerService.startOtaSim(simResult: .none)//without error
            //Evs.instance().developer().startOtaSim(simResult: .commerror)//simulate error
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

extension OtaHandlingViewController : IEvsOtaAvailableHandler {
    func onOtaAvailable(version: Int32) {
        let alert = UIAlertController(title: "System update available\(txt)", message: "Do you want to install system update with version \(version)", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Yes", style: .default, handler: { (_) -> Void in
            Evs.instance().ota().startOtaProcess()
        }))
        alert.addAction(UIAlertAction(title: "No", style: .cancel, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
}

extension OtaHandlingViewController : IEvsOtaEventsHandler {
    
    func onOtaFailed(errCode: OtaErrorCode, description: String) {
        showMessage("Failed: \(description)")
    }
    
    func onOtaInstallCompleted() {
        showMessage("Glasses are up to date")
    }
    
    func onOtaInstallStarted() {
        showMessage("Installing")
    }
    
    func onOtaProgress(progress: Int32) {
        showMessage("Uploading \(progress)%")
    }
    
    func onOtaStarted(filesCount: Int32) {
        showMessage("Starting Upload \(filesCount) file(s)")
    }
    
    func onOtaUploadCompleted() {
        showMessage("Upload Completed")
    }
    
}

extension OtaHandlingViewController : IEvsCommunicationEvents {
    func onFailedToConnect() {
        showMessage("Disconnected")
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

extension OtaHandlingViewController : IEvsAppEvents {
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
