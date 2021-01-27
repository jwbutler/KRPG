package com.jwbutler.krpg

import com.jwbutler.gameengine.graphics.GameWindow
import com.jwbutler.gameengine.graphics.ImageLoader
import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.RPGGameView
import com.jwbutler.krpg.core.Singletons
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.RPGEquipmentSlot
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.levels.LEVEL_ONE
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.MousePlayer
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.sounds.SoundPlayer

fun main()
{
    val gameView = GameView.initialize { RPGGameView() }
    Singletons.register(ImageLoader::class.java, ImageLoader.create()) // { filename -> "/png/${filename}.png" })
    val state = GameState.initialize()
    Singletons.register(GameWindow::class.java, GameWindow.create(gameView.gameDimensions, gameView.initialWindowDimensions))
    val engine = GameEngine.initialize()
    SoundPlayer.initialize { SoundPlayer { filename -> "/sounds/${filename}.wav" } }

    val humanPlayer = MousePlayer()
    state.addPlayer(humanPlayer)
    val enemyPlayer = EnemyPlayer()
    state.addPlayer(enemyPlayer)

    engine.startGame(LEVEL_ONE, _getInitialUnits())
}

private fun _getInitialUnits(): List<GameEngine.UnitData>
{
    val state = GameState.getInstance()
    val humanPlayer = state.getHumanPlayer()

    val paletteSwaps = PaletteSwaps()
        .put(Colors.GREEN, Colors.RED)
        .put(Colors.DARK_GREEN, Colors.DARK_RED)

    return listOf(
        GameEngine.UnitData(
            PlayerUnit(200, paletteSwaps),
            mapOf(
                RPGEquipmentSlot.MAIN_HAND to Sword(),
                RPGEquipmentSlot.OFF_HAND to Shield(),
                RPGEquipmentSlot.CHEST to MailArmor()
            )
        ),
        GameEngine.UnitData(
            PlayerUnit(200, paletteSwaps),
            mapOf(
                RPGEquipmentSlot.MAIN_HAND to Sword(),
                RPGEquipmentSlot.OFF_HAND to Shield(),
                RPGEquipmentSlot.CHEST to MailArmor()
            )
        )
    )
}