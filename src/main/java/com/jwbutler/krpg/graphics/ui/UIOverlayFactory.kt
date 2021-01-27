package com.jwbutler.krpg.graphics.ui

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.graphics.Image
import com.jwbutler.krpg.utils.rectFromPixels
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.krpg.graphics.Colors

object UIOverlayFactory
{
    fun getSelectionRect(start: Pixel, end: Pixel): Renderable
    {
        val rectangle = rectFromPixels(start, end)
        val image = Image.create(rectangle.width + 1, rectangle.height + 1)
        image.drawRect(rectangle, Colors.GREEN)

        return Renderable(image, Pixel(rectangle.left, rectangle.top), RenderLayer.UI_OVERLAY)
    }
}