//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class AnimationsScreen:Screen {
    private var cnt = 0
    private let text = Text()
    private let simpleCtrl1 = SimpleControl()
    private let simpleCtrl2 = SimpleControl()
    override func onCreate() {
        
        text.setText(text: "Animations")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
            .setX(x: getWidth()/2)
            .setY(y: getHeight()/2)
            .setForegroundColor(color: EvsColor.green.rgba)
        
        add(uiElement: text)
        
        simpleCtrl1.setWidth(width: 30.0)
            .setHeight(height: 30.0)
            .setX(x: 30.0)
            .setY(y: 30.0)
        
        add(uiElement: simpleCtrl1)
        
        simpleCtrl2
            .setWidth(width: 70.0)
            .setHeight(height: 30.0)
            .setX(x: 230.0)
            .setY(y: 130.0)
        
        add(uiElement: simpleCtrl2)
    }
    
    override func onResume() {
        super.onResume()
        //NOTE: text and image ui elements can be animated only with translation (no scale, no rotate)
        text.animator().duration(ms: 3000).translateTo(x: text.getX(), y: 0).reverse().start()
        
        simpleCtrl1.animator()
            .scaleX(scaleFactor: 2.0)
            .scaleY(scaleFactor: 0.5)
            .duration(ms: 5000)
            .translateTo(x: 130.0,y: 30.0)
            .bounce()
            .start()
        
        simpleCtrl2.animator()
            .scale(scaleFactor: 1.5)
            .rotate(angleDegrees: 45.0)
            .repeat()
            .duration(ms: 1000)
            .register(listener: self)
            .start()
    }
}

extension AnimationsScreen : AnimatorIEvsAnimationListener {
    func animationEnded(uiElement: UIElement) {
        cnt+=1
    }
    
    func animationLooped(uiElement: UIElement) {
        cnt+=1
    }
    
    func animationStarted(uiElement: UIElement) {
        
    }
    
    func animationStep(uiElement: UIElement, progress: Double) {
        self.text.setText(text: "Loop \(cnt) \(round(progress * 100))%")
    }
}
