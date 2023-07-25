//Copyright (c) 2020 Everysight LTD

import Foundation
import EvsKit
import NativeEvsKit

class ImagesBatchScreen:Screen {

    private let text = Text()
    private var images : [Image]? = nil

    override func onCreate() {
        super.onCreate()
        
        text
            .setResource(font: Font(stockFont: .small))
            .setCenter(x: getWidth()/2)
            .setY(y: getHeight()/3)

        add(uiElement: text)
        images = [Image]()
        let img1 = Image(id: "blast")
            .setResource(image: ImgSrc(nameWithExtension: "blast.jpg", slot: .s1))
        images?.append(img1)
        
        let img2 = Image(id: "heart")
            .setResource(image: ImgSrc(nameWithExtension: "heart.jpg", slot: .s2))
        images?.append(img2)

        let img3 = Image(id: "ship")
            .setResource(image: ImgSrc(nameWithExtension: "ship.jpg", slot: .s3))
        images?.append(img3)
        
        let img4 = Image(id: "watermelon")
            .setResource(image: ImgSrc(nameWithExtension: "watermelon.jpg", slot: .s4))
        images?.append(img4)
        
        let img5 = Image(id: "orange")
            .setResource(image: ImgSrc(nameWithExtension: "orange.jpg", slot: .s5))
        images?.append(img5)
        
        if let imgs = images {
            requestResourcesUpload(resources: imgs.getResources())
        }
        text.setVisibility(isVisible: true)
        text.setText(text: "Loading...")
    }

    
    override func onResourceUploadResult(resource: UIResource) {
        text.setText(text: "\(resource.getResourceName()) was loaded")
    }

    override func onResourcesUploadEnd() {
        super.onResourcesUploadEnd()
        text.setVisibility(isVisible: false)
        var i = 0
        if let imageArray = images {
            imageArray.forEach { img in
                img.setX(x: Float(i)*50.0).setY(y: getHeight()/2)
                add(uiElement: img)
                i+=1
            }
        }
    }

}

extension Array where Element == Image {
    func getResources() -> [ImgSrc] {
        var srcs = [ImgSrc]()
        for i in self {
            if let s = i.getResource() {
                srcs.append(s)
            }
        }
        return srcs
    }
}
