//Copyright (c) 2020 Everysight LTD

import Foundation
import EvsKit
import NativeEvsKit


class ImagesHandlingScreen:Screen {

    override func onCreate() {
        let banana = Image()
            .setResource(image: ImgSrc(nameWithExtension: "banana.jpg", slot: .s1))
            .setX(x: 70.0)
            .setY(y: 130.0)
        add(uiElement: banana)

        let apple = Image()
            .setResource(image: ImgSrc(nameWithExtension: "apple.jpg", slot: .s2))
            .setX(x: 200.0)
            .setY(y: 70.0)
        add(uiElement: apple)
    }
}
