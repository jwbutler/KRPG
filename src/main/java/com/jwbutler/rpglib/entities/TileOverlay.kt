package com.jwbutler.rpglib.entities

import com.jwbutler.gameengine.graphics.Color
import com.jwbutler.gameengine.graphics.ImageLoader
import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.core.Singletons
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.rpglib.graphics.sprites.Sprite
import com.jwbutler.rpglib.graphics.sprites.StaticSprite

class TileOverlay(
    private val coordinates: Coordinates,
    private val outerColor: Color,
    private val innerColor: Color
): Entity
{
    companion object
    {
        private val INNER_COLOR = Colors.BLACK
        private val OUTER_COLOR = Colors.RED
    }

    override fun getCoordinates() = coordinates
    override val sprite = _getSprite()

    private fun _getSprite(): Sprite
    {
        val paletteSwaps = PaletteSwaps()
            .put(OUTER_COLOR, outerColor)
            .put(INNER_COLOR, innerColor)

        return StaticSprite(
            Singletons.get(ImageLoader::class.java).loadImage("tiles/overlay_24x12", paletteSwaps),
            RenderLayer.FLOOR_TILE
        )
    }

    override fun render() = sprite.render(this)
}
