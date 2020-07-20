package com.jwbutler.krpg.entities

import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.RenderLayer
import com.jwbutler.krpg.graphics.Renderable
import com.jwbutler.krpg.graphics.sprites.StaticSprite

class Tile(private val coordinates: Coordinates) : Entity
{
    /**
     * TODO - tile types, palette swaps, caching, etc.
     */
    private val sprite = StaticSprite(
        ImageLoader.getInstance().loadImage(
            // "tiles/48x23/tile_48x23_stone",
            //"tiles/tile_floor",
            "tiles/tile_floor5",
            PaletteSwaps()
        ),
        RenderLayer.FLOOR_TILE
    )

    override fun getCoordinates() = coordinates
    override fun getSprite() = sprite
    override fun update() {}
    override fun afterRender() {}

    override fun render(): Renderable
    {
        return sprite.render(this)
    }
}