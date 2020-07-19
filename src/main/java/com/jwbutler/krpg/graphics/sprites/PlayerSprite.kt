package com.jwbutler.krpg.graphics.sprites

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.graphics.UnitFrame
import com.jwbutler.krpg.graphics.PaletteSwaps

class PlayerSprite(paletteSwaps: PaletteSwaps) : UnitSprite("player", paletteSwaps)
{
    override fun _getFrames(activity: Activity, direction: Direction): List<UnitFrame>
    {
        return when (activity)
        {
            // TODO  these are all made up
            Activity.STANDING -> (1..4).map { UnitFrame(activity, direction, "1") }
            Activity.WALKING -> arrayOf(1, 1, 2, 2).map { UnitFrame(activity, direction, it.toString()) }
            Activity.ATTACKING -> arrayOf(1, 1, 2, 2).map { UnitFrame(activity, direction, it.toString()) }
            Activity.FALLING -> arrayOf(1, 1, 2, 2).map { UnitFrame(activity, direction, it.toString()) }
            else -> error("Invalid activity ${activity}")
        }
    }
}