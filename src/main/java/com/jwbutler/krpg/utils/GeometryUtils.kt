package com.jwbutler.krpg.utils

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.geometry.Rectangle
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.geometry.Coordinates
import java.awt.Point
import kotlin.math.roundToInt

fun getAdjacentCoordinates(coordinates: Coordinates): Set<Coordinates>
{
    val adjacentCoordinates = mutableSetOf<Coordinates>()
    for (dy in -1..1)
    {
        for (dx in -1..1)
        {
            val candidate =
                Coordinates(coordinates.x + dx, coordinates.y + dy)
            if (!candidate.isBlocked())
            {
                adjacentCoordinates.add(candidate)
            }
        }
    }
    val state = GameState.getInstance()
    return adjacentCoordinates
        .filter(state::containsCoordinates)
        .filter { it != coordinates }
        .toSet()
}

fun getAdjacentUnblockedCoordinates(coordinates: Coordinates): Set<Coordinates>
{
    return getAdjacentCoordinates(coordinates).filter { !it.isBlocked() }.toSet()
}

fun rectFromPixels(first: Pixel, vararg rest: Pixel): Rectangle
{
    val rect = java.awt.Rectangle(Point(first.x, first.y))
    for (pixel in rest)
    {
        rect.add(pixel.x, pixel.y)
    }
    return Rectangle(rect.x, rect.y, rect.width, rect.height)
}

fun getAverageCoordinates(coordinates: Collection<Coordinates>): Coordinates
{
    val x = coordinates.map(Coordinates::x).average().roundToInt()
    val y = coordinates.map(Coordinates::y).average().roundToInt()
    return Coordinates(x, y)
}