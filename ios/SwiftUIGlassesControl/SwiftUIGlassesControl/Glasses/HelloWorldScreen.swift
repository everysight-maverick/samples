import EvsKit
import NativeEvsKit

class HelloWorldScreen:Screen{
    override func onCreate() {
        super.onCreate()
        
        let text1 = Text()
        text1.setText(text: "Hello")
    
        let text2 = Text()
        text2.setText(text: "World").setForegroundColor(evsColor:EvsColor.orange)
        
        Column(verticalArrangement: .center, horizontalAlignment: .center, spacedBy: 20.0)
            .addAll(uiElements:
            [
                Text().setText(text: "My Screen").setForegroundColor(evsColor: EvsColor.green),
                Row(horizontalArrangement: .center, verticalAlignment: .center,spacedBy: 5.0)
                    .addAll(uiElements:[text1,text2])
                    .setWidth(width: self.getWidth())
            ]
        )
        .setWidthHeight(width: self.getWidth(), height: self.getHeight())
        .addTo(screen: self)
    }
}
