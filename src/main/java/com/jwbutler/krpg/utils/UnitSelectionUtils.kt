package com.jwbutler.krpg.utils

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.geometry.Rectangle
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.players.HumanPlayer
import com.jwbutler.rpglib.players.Player

fun getPlayerUnits(): List<Unit>
{
    val humanPlayer = GameState.getInstance()
        .getPlayers()
        .filterIsInstance<HumanPlayer>()
        .firstOrNull()
        ?: error("Could not find human player")
    return humanPlayer.getUnits() // TODO: Sorting?
}

fun getEnemyUnits(): Collection<Unit>
{
    return GameState.getInstance()
        .getPlayers()
        .filterNot { it is HumanPlayer }
        .flatMap(Player::getUnits)
}

fun getUnitsInPixelRect(rect: Rectangle): Collection<Unit>
{
    val gameView = GameView.getInstance()
    val tileDimensions = gameView.tileDimensions
    val tileWidth = tileDimensions.width
    val tileHeight = tileDimensions.height

    val selectedUnits = mutableListOf<Unit>()
    val allUnits = GameState.getInstance().getUnits()
    for (unit in allUnits)
    {
        val pixel = unit.getCoordinates().toPixel() + Pixel(tileWidth / 2, tileHeight / 2) // TODO second term shouldn't be a pixel
        if (rect.contains(pixel.x, pixel.y))
        {
            selectedUnits.add(unit)
        }
    }
    return selectedUnits
}