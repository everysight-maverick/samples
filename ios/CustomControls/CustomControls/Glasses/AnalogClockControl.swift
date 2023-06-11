//Copyright (c) 2020 Everysight LTD

import Foundation
#if os(watchOS)
import WatchNativeEvsKit
#else
import NativeEvsKit
#endif

class AnalogClockControl : Frame {
    
    private let OUTER_WIDTH:Float = 3
    private var padding:Float = 0.0
    private var clockCenter = Ellipse(id: "clockCenter")
    private var hours = Line(id: "hours")
    private var minutes = Line(id: "minutes")
    private var seconds = Line(id: "seconds")
    private var radius:Float = 0.0
    private var hPrev = -1
    private var mPrev = -1
    private var sPrev = -1
    private var date = Text(id: "date")
    
    private var centerX:Float = 0.0
    private var centerY:Float = 0.0
    private lazy var timeFormat:DateFormatter = {
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "HH:mm:ss"
        return dateFormatterPrint
    }()
    
    private lazy var dateFormat:DateFormatter = {
        let dateFormatterPrint = DateFormatter()
        dateFormatterPrint.dateFormat = "dd-MM-yyyy"
        return dateFormatterPrint
    }()
    
    override func onCreate() {
        super.onCreate()
        
        centerX = getWidth()/2
        centerY = getHeight()/2
        radius = min(centerX,centerY)
        padding = radius/20.0
        
        addTicks()
        
        hours
            .toAngle(length: radius-OUTER_WIDTH-7*padding,angle: 0)
            .setPenThickness(thicknessPixels: 4)
            .setForegroundColor(color: EvsColor.green.rgba)
            .setX(x: centerX).setY(y: centerY)
        minutes
            .toAngle(length: radius-OUTER_WIDTH-padding,angle: 0)
            .setPenThickness(thicknessPixels: 4)
            .setForegroundColor(color: EvsColor.green.rgba)
            .setX(x: centerX).setY(y: centerY)
        seconds
            .toAngle(length: radius-OUTER_WIDTH+3*padding,angle: 0)
            .setPenThickness(thicknessPixels: 2)
            .setForegroundColor(color: EvsColor.blue.rgba)
            .setX(x: centerX).setY(y: centerY+4*padding)
        
        add(uiElement: hours)
        add(uiElement: minutes)
        add(uiElement: seconds)
        
        clockCenter
            .setEllipseInfo(centerX: centerX,centerY: centerY,radius: 2*padding)
            .setFillColor(color: EvsColor.blue.rgba)
            .setForegroundColor(color: EvsColor.black.rgba)
            .setPenThickness(thicknessPixels: Int32(padding))
        add(uiElement: clockCenter)
    }
    
    private func addTicks() {
        let arr = [1.0,2.0,4.0,5.0,7.0,8.0,10.0,11.0]
        let PI = 3.14
        for i in arr {
            let angle = 360.0*i/12.0
            let rad = angle * (PI / 180.0)
            
            let rx:Double = Double(centerX) * sin(rad)
            let ry:Double = Double(centerY) * cos(rad)
            let r:Double = sqrt(rx*rx+ry*ry)
            let t = Arc(id: "H$i")
            t.setArcInfo(centerX: centerX, centerY: centerY, radius: Float(r-3), angleFromDeg: Float(angle-1), angleToDeg: Float(angle+1))
            t.setPenThickness(thicknessPixels: 7)
            t.setForegroundColor(color: EvsColor.green.rgba)
            
            add(uiElement: t)
        }
        
        let t12 = Text()
        t12
            .setText(text: "12")
            .setResource(font: Font(stockFont: .small))
            .setTextAlign(align: Align.center)
            .setX(x: centerX)
            .setY(y: 0.0 + padding)
        add(uiElement: t12)
        
        let t3 = Text()
        t3.setText(text: "3")
            .setResource(font: Font(stockFont: .small))
            .setTextAlign(align: Align.right)
            .setX(x: centerX * 2 - padding)
            .setY(y: centerY - 6)
        add(uiElement: t3)
        
        let t6 = Text()
        t6.setText(text: "6")
            .setResource(font: Font(stockFont: .small))
            .setTextAlign(align: Align.center)
            .setX(x: centerX)
            .setY(y: 2 * centerY - 8 - padding)
        add(uiElement: t6)
        
        let t9 = Text()
        t9.setText(text: "9")
            .setResource(font: Font(stockFont: .small))
            .setTextAlign(align: Align.left)
            .setX(x: 0.0 + padding)
            .setY(y: centerY - 6)
        add(uiElement: t9)
        
        date
            .setText(text: "")
            .setResource(font: Font(stockFont: .small))
            .setTextAlign(align: Align.center)
            .setX(x: centerX)
            .setY(y: centerY + 5 * padding)
        add(uiElement: date)
        
    }
    override func onBeforeDraw(timestampMs: Int64) {
        super.onBeforeDraw(timestampMs: timestampMs)
        updateTime()
    }
    
    private func updateTime() {
        let d = Date()
        let calendar = Calendar.current
        let components = calendar.dateComponents([.hour, .minute, .second], from: d)
        
        let h:Double = Double(components.hour!)
        let m:Double = Double(components.minute!)
        let s:Double = Double(components.second!)
        
        let dText = dateFormat.string(from: Date())
        date.setText(text: dText)
        
        var angle = ( 360.0 * (s+m*60+h*60*60) / (60.0*60.0*12.0) )
        if(Int(angle) != hPrev) {
            hours.rotate(angleDegrees: Float(angle), px: 0, py: 0, pivot: .relativetoself)
            hPrev = Int(angle)
        }
        
        angle = 360.0 * (s+m*60.0) / (60*60.0)
        if(Int(angle) != mPrev) {
            minutes.rotate(angleDegrees: Float(angle), px: 0, py: 0, pivot: .relativetoself)
            mPrev = Int(angle)
        }
        
        angle = 360.0 * s / (60.0)
        if(Int(angle) != sPrev) {
            seconds.rotate(angleDegrees: Float(angle), px: 0, py: -(4*padding), pivot: .relativetoself)
            sPrev = Int(angle)
        }
    }
}
        
