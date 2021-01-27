package com.jwbutler.krpg.entities.units

import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.graphics.sprites.units.ZombieSprite
import com.jwbutler.rpglib.behavior.Activity
import kotlin.random.Random

private val ACTIVITIES = setOf(
    RPGActivity.ATTACKING,
    RPGActivity.FALLING,
    RPGActivity.STANDING,
    RPGActivity.WALKING
)
class ZombieUnit(hp: Int) : AbstractUnit(hp, ACTIVITIES)
{
    override val sprite = ZombieSprite(PaletteSwaps().withTransparentColor(Colors.WHITE))
    override fun getCooldown(activity: Activity): Int
    {
        return when (activity)
        {
            RPGActivity.WALKING   -> Random.nextInt(1, 11)
            RPGActivity.ATTACKING -> Random.nextInt(1, 11)
            else                  -> 0
        }
    }
}