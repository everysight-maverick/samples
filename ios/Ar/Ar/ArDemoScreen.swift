import Foundation
import EvsKit
import NativeEvsKit

class ArDemoScreen: ArScreen {
    private let TAG = "ArDemoScreen"
    private let responses = ["COOL!", "WOW!", "NICE ONE", "WELL DONE", "PERFECT", "AMAZING"]
    private var tapText: HudText?
    private var worldPos: KotlinFloatArray = KotlinFloatArray(size: 3)
    private var cubes: [ArDrawInfo] = []
    private var texts: [HudText] = []
    private var quatOK = false
    
    override init() {
        for i in 0..<worldPos.size {
            worldPos.set(index: i, value: 0.0)
        }
        super.init()
    }
    
    override func onCreate() {
        super.onCreate()
        Evs.instance().sensors().enableTouch(enable: true)
    }
    
    override func onUpdateUI(timestampMs: Int64) {
        super.onUpdateUI(timestampMs: timestampMs)
        if (tapText == nil) {
            tapText = HudText(txt: "LOADING...")
            tapText!.setX(x: getWidth() / 2 - tapText!.getWidth() / 2)
            tapText!.setY(y: getHeight() / 2 - tapText!.getHeight() / 2)
            add(uiElement: tapText!)
        }
        if (!quatOK && quat.isValid && !quat.isIdentity) {
            quatOK = true
            tapText!.setText("TAP TO PLACE ELEMENTS")
        }
        for i in 0..<cubes.count {
            cubes[i].angle += Float(cubes[i].direction) * 0.8
            cubes[i].model.rotate(east: cubes[i].angle, north: 0.0, up: 45.0)
        }
        tapText!.setEyePosition(qtrn: quat)
    }
    
    override func onTouch(touchDirection: TouchDirection) -> Bool {
        super.onTouch(touchDirection: touchDirection)
        if (touchDirection == .tap) {
            if (quat.isValid && !quat.isIdentity) {
                print(TAG, "TAP")
                tapText?.removeSelf()
                
                if (cubes.count > 0 && Float.random(in: 0.0..<1.0) > 0.65) {
                    let t = HudText(txt: responses[Int.random(in: 0..<responses.count)])
                    t.setEyePosition(qtrn: quat)
                    add(uiElement: t)
                    texts.append(t)
                    return true
                }
                
                let isCube = Float.random(in: 0.0..<1.0) > 0.3
                let c = isCube ? ArFactory.companion.makeCube() : ArFactory.companion.makeArrow()
                let scale = isCube ? 0.25 : 0.3
                c.scale(factor: Float(scale))
                c.rotate(east: 0.0, north: 0.0, up: 45.0)
                
                eye2world(xEye: 0.0, yEye: 0.0, zEye: -20.0 * Float.random(in: 0.3..<(1.5)), q: quat, res: worldPos)
                c.translate(east: worldPos.get(index: 0), north: worldPos.get(index: 1), up: worldPos.get(index: 2))
                add(arElement: c)
                cubes.append(ArDrawInfo(model: c, angle: 0.0, direction: Bool.random() ? 1 : -1))
            }
        }
        return true
    }
    
    /// Transforms eye coordinates to world coordinates using a quaternion.
    ///
    /// - Parameters:
    ///   - xEye: The x-coordinate in the eye's coordinate system
    ///   - yEye: The y-coordinate in the eye's coordinate system
    ///   - zEye: The z-coordinate in the eye's coordinate system
    ///   - q: The quaternion representing the rotation
    ///   - res: The resulting world 3x1 vector
    func eye2world(xEye: Float, yEye: Float, zEye: Float, q: Quaternion, res: KotlinFloatArray) {
        q.rotateVector(x: xEye, y: yEye, z: zEye, out: res, offset: 0)
    }
    
    class HudText: ArWindow {
        private var txt: String
        private let text = Text()
        
        init (txt: String) {
            self.txt = txt
            super.init()
        }
        
        override func onCreate() {
            super.onCreate()
            setText(txt)
            setBorderStyle(color: EvsColor.green.rgba, widthPix: 2, cornerRadiusPix: 10.0)
            showBorder(show: true)
            add(uiElement: text)
            setBackgroundColor(evsColor: EvsColor.black)
        }
        func setText(_ txt: String) {
            self.txt = txt
            text.setText(text: txt)
            
            setWidthHeight(width: text.getWidth() * 1.2, height: text.getHeight() * 1.2)
            
            text.setCenter(x: getWidth()/2)
            text.setY(y: getHeight() / 2 - text.getHeight() / 2 + 2)
            text.setForegroundColor(evsColor: EvsColor.yellow)
        }
    }
    
    struct ArDrawInfo {
        let model: ArModel
        var angle: Float
        var direction: Int
    }
}
