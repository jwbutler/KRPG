package com.jwbutler.krpg.graphics.ui

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.geometry.Rectangle
import com.jwbutler.gameengine.graphics.Image
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable
import com.jwbutler.krpg.graphics.Colors

class HUDRenderer(width: Int, height: Int)
{
    companion object
    {
        const val HEIGHT = 40 // If HEIGHT = 180, then this is ~22%
    }

    val image = Image.create(width, height)

    fun render(): Renderable
    {
        _renderBackground()

        var x = 5
        for (unit in getPlayerUnits())
        {
            val card = UnitCard(unit)
            val cardImage = card.render()
            image.drawImage(cardImage, x, 5)
            x += UnitCard.WIDTH + 5
        }

        val gameDimensions = GameView.getInstance().gameDimensions
        // TODO - layer is kind of superfluous here since it's getting added at the end
        return Renderable(image, Pixel(0, gameDimensions.height - HEIGHT), RenderLayer.UI_OVERLAY)
    }

    private fun _renderBackground()
    {
        val gameDimensions = GameView.getInstance().gameDimensions
        val gameWidth = gameDimensions.width
        image.fillRect(Rectangle(0, 0, gameWidth - 1, HEIGHT - 1), Colors.BLACK)
        image.drawRect(Rectangle(0, 0, gameWidth - 1, HEIGHT - 1), Colors.WHITE)
    }
}
