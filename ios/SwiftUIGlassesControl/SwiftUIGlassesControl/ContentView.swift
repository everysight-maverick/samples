import SwiftUI
import EvsKit
import NativeEvsKit

struct ContentView: View {
    var body: some View {
        VStack {
            GlassesInfoView()
            Text("Long press to reconfigure")
        }
        .padding()
        .onAppear() {
            Evs.instance().start()
            Evs.instance().registerAppEvents(listener: AppEvents())
            Evs.instance().comm().connect()
            Evs.instance().screens().addScreen(screen: HelloWorldScreen())
        }
    }
}

class AppEvents : IEvsAppEvents {
    func onBeginAuth(serial: String, fwVersion: Int32) {
       

    }
    func onBeforeRendering(timestamp: Int64) {
     
    }
    
    func onReady() {
        Evs.instance().display().turnDisplayOn()
    }
    
    func onUnReady() {
        
    }
    
    func onError(errCode: AppErrorCode, description: String) {

    }
}

#Preview {
    ContentView()
}
