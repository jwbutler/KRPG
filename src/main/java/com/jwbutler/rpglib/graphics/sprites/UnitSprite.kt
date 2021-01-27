package com.jwbutler.rpglib.graphics.sprites

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.graphics.ImageLoader
import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.core.Singletons
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.entities.Entity
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.graphics.FrameKey
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.Renderable

abstract class UnitSprite
(
    private val spriteName: String,
    private val paletteSwaps: PaletteSwaps
) : Sprite
{
    override fun render(entity: Entity): Renderable
    {
        val unit = entity as Unit
        val coordinates = unit.getCoordinates()
        val frame = _getFrame(unit.getActivity(), unit.getDirection(), unit.getFrameNumber())
        val filename = _formatFilename(frame)
        val image = Singletons.get(ImageLoader::class.java).loadImage(filename, paletteSwaps)
        val pixel = coordinates.toPixel() + Pixel(offsets.x, offsets.y) // TODO second term shouldn't be a Pixel

        return Renderable(image, pixel, RenderLayer.UNIT)
    }

    fun isAnimationComplete(unit: Unit): Boolean
    {
        val frames = _getFrames(unit.getActivity(), unit.getDirection())
        return unit.getFrameNumber() > frames.lastIndex
    }

    private fun _getFrame(activity: Activity, direction: Direction, frameNumber: Int): FrameKey
    {
        val frames = _getFrames(activity, direction)
        return frames[frameNumber]
    }

    private fun _formatFilename(frameKey: FrameKey): String
    {
        return "units/${spriteName}/${spriteName}_${frameKey.keys.joinToString("_")}"
    }

    protected abstract fun _getFrames(activity: Activity, direction: Direction): List<FrameKey>
}