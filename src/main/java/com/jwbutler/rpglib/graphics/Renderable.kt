package com.jwbutler.rpglib.graphics

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.graphics.Image

/**
 * Represents an image and all data directly necessary to render it.
 */
data class Renderable
(
    val image: Image,
    val pixel: Pixel,
    val layer: RenderLayer
)