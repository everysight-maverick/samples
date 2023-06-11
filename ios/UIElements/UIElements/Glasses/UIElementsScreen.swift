//Copyright (c) 2020 Everysight LTD

import Foundation
import NativeEvsKit

class UIElementsScreen:Screen {

    override func onCreate() {
        let text = Text()
        text.setText(text: "UI Elements Screen")
            .setResource(font: Font(stockFont: .medium))
            .setTextAlign(align: Align.center)
            .setX(x: getWidth()/2)
            .setY(y: 10)
            .setForegroundColor(color: EvsColor.green.rgba)
        add(uiElement: text)

        let textLeft = Text()
        textLeft.setText(text: "Small Left Text")
            .setResource(font: Font(stockFont: .small))
            .setTextAlign(align: Align.left)
            .setX(x: 10.0).setY(y: 100.0)
        add(uiElement: textLeft)

        let textRight = Text()
        textRight.setText(text: "Large Right Text")
            .setResource(font: Font(stockFont: .large))
            .setTextAlign(align: Align.right)
            .setX(x: getWidth()-10.0)
            .setY(y: getHeight()-textRight.getHeight()-50.0)
        add(uiElement: textRight)

        let line = Line()
        line.toAngle(length: getWidth()-20.0,angle: 90.0)
            .setPenThickness(thicknessPixels: 4)
            .setForegroundColor(color: EvsColor.green.rgba)
            .setX(x:20.0).setY(y: getHeight()/2)
        add(uiElement: line)

        let rect = Rect()
        rect.setX(x: 30.0)
            .setY(y: 50.0)
            .setWidth(width: 50.0)
            .setHeight(height: 30)
            .setBackgroundColor(color: EvsColor.blue.rgba)
        add(uiElement: rect)

        let polygon = Polygon()
        polygon
            .addPoint(x: 10.0, y:80.0)
            .addPoint(x: 50.0, y:100.0)
            .addPoint(x: 30.0, y:110.0)
            .addPoint(x: 20.0, y:60.0)
            .setX(x: 110.0)
        add(uiElement: polygon)


        let path = Path()
        path
            .moveTo(x: 200.0, y: 55.0)
            .lineTo(x: 200.0, y: 70.0)
            .lineTo(x: 210.0, y: 65.0)
            .moveTo(x: 230.0, y: 55.0)
            .lineTo(x: 230.0, y: 70.0)
            .lineTo(x: 240.0, y: 65.0)
        add(uiElement: path)

        let ellipse = Ellipse()
        ellipse.setEllipseInfo(centerX: 215.0,centerY: 80.0,radius: 40.0)
        add(uiElement: ellipse)

        let arc = Arc()
        arc.setArcInfo(centerX: 215.0,centerY: 80.0,radius: 20.0,angleFromDeg: 150.0,angleToDeg: 210.0)
        arc.setPenThickness(thicknessPixels: 4).setForegroundColor(color: EvsColor.red.rgba)
        add(uiElement: arc)

    }
}
