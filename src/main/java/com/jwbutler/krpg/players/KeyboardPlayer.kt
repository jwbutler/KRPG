package com.jwbutler.krpg.players

import com.jwbutler.gameengine.input.KeyboardKey
import com.jwbutler.gameengine.input.KeyboardListener
import com.jwbutler.gameengine.input.ModifierKey
import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.players.HumanPlayer
import kotlin.math.abs

class KeyboardPlayer : HumanPlayer()
{
    private val queuedDirections = mutableSetOf<KeyboardKey>()
    private val heldDirections = mutableSetOf<KeyboardKey>()
    private val heldModifiers = mutableSetOf<KeyboardKey>()

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        var (dx, dy) = Pair(0, 0)

        for (directionKey in queuedDirections)
        {
            when (directionKey)
            {
                KeyboardKey.W -> dy--
                KeyboardKey.A -> dx--
                KeyboardKey.S -> dy++
                KeyboardKey.D -> dx++
                else -> {}
            }
        }
        queuedDirections.clear()

        if (dx != 0) dx /= abs(dx)
        if (dy != 0) dy /= abs(dy)
        val coordinates = Coordinates(
            unit.getCoordinates().x + dx,
            unit.getCoordinates().y + dy
        )

        if ((dx != 0 || dy != 0) && GameState.getInstance().containsCoordinates(coordinates))
        {
            val isShiftDown = heldModifiers.contains(KeyboardKey.LEFT_SHIFT) || heldModifiers.contains(KeyboardKey.RIGHT_SHIFT)
            if (isShiftDown && unit.isActivityReady(RPGActivity.ATTACKING))
            {
                return Pair(RPGActivity.ATTACKING, Direction.from(dx, dy))
            }
            else if (unit.isActivityReady(RPGActivity.WALKING))
            {
                return Pair(RPGActivity.WALKING, Direction.from(dx, dy))
            }
        }
        return Pair(RPGActivity.STANDING, unit.getDirection())
    }

    override fun getKeyListener() = object : KeyboardListener
    {
        override fun keyDown(key: KeyboardKey, modifiers: Set<ModifierKey>)
        {
            when (key)
            {
                KeyboardKey.W, KeyboardKey.A, KeyboardKey.S, KeyboardKey.D ->
                {
                    if (!heldDirections.contains(key))
                    {
                        queuedDirections.add(key)
                        heldDirections.add(key)
                    }
                }
                KeyboardKey.LEFT_SHIFT, KeyboardKey.RIGHT_SHIFT ->
                {
                    heldModifiers.add(key)
                }
            }
        }

        override fun keyUp(key: KeyboardKey, modifiers: Set<ModifierKey>)
        {
            when (key)
            {
                KeyboardKey.W, KeyboardKey.A, KeyboardKey.S, KeyboardKey.D ->
                {
                    heldDirections.remove(key)
                }
                KeyboardKey.LEFT_SHIFT, KeyboardKey.RIGHT_SHIFT ->
                {
                    heldModifiers.remove(key)
                }
            }
        }
    }

    private fun _getUnit() = GameState.getInstance().getUnits(this)[0]
}