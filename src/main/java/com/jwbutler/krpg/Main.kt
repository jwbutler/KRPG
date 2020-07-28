package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.GameState
import com.jwbutler.krpg.entities.Tile
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.objects.Wall
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.entities.units.WizardUnit
import com.jwbutler.krpg.entities.units.ZombieUnit
import com.jwbutler.krpg.geometry.Coordinates
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.graphics.GameRenderer
import com.jwbutler.krpg.graphics.GameWindow
import com.jwbutler.krpg.graphics.ImageLoader
import com.jwbutler.krpg.graphics.PaletteSwaps
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.KeyboardPlayer
import com.jwbutler.krpg.players.MousePlayer
import java.lang.Thread.sleep

fun main()
{
    ImageLoader.initialize()
    val window = GameWindow.initialize()

    val state = GameState.initialize()
    state.setTiles(_tileBox(15, 15))
    val renderer = GameRenderer(window)
    val engine = GameEngine(state, renderer)

    val paletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
        .put(Colors.GREEN, Colors.RED)
        .put(Colors.DARK_GREEN, Colors.DARK_RED)

    val humanPlayer = MousePlayer()
    //val humanPlayer = KeyboardPlayer()
    val playerUnit = PlayerUnit(humanPlayer, Coordinates(4, 4), 200, paletteSwaps)
    playerUnit.addEquipment(Sword())
    playerUnit.addEquipment(Shield())
    playerUnit.addEquipment(MailArmor())

    Wall(Coordinates(8, 3))
    Wall(Coordinates(8, 4))
    Wall(Coordinates(8, 5))
    Wall(Coordinates(8, 6))
    Wall(Coordinates(8, 7))

    val enemyPlayer = EnemyPlayer()
    val enemyUnit = PlayerUnit(enemyPlayer, Coordinates(12, 5), 50)
    enemyUnit.addEquipment(Sword())
    val enemyZombie = ZombieUnit(enemyPlayer, Coordinates(12, 7), 50)
    val enemyWizard = WizardUnit(enemyPlayer, Coordinates(8, 9), 50)

    // TODO - should use a real event loop here
    while (true)
    {
        engine.doLoop()
        sleep(100)
    }
}

private fun _tileBox(width: Int, height: Int): Map<Coordinates, Tile?>
{
    val tiles: MutableMap<Coordinates, Tile?> = mutableMapOf()
    for (y in (0 until height))
    {
        for (x in (0 until width))
        {
            val coordinates = Coordinates(x, y)
            tiles.put(coordinates, Tile(coordinates))
        }
    }
    return tiles.toMap()
}