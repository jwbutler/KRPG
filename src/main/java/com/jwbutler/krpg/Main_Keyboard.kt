package com.jwbutler.krpg

import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.KeyboardGameView
import com.jwbutler.krpg.entities.equipment.MailArmor
import com.jwbutler.krpg.entities.equipment.RPGEquipmentSlot
import com.jwbutler.krpg.entities.equipment.Shield
import com.jwbutler.krpg.entities.equipment.Sword
import com.jwbutler.krpg.entities.units.PlayerUnit
import com.jwbutler.krpg.levels.LEVEL_ONE
import com.jwbutler.krpg.players.EnemyPlayer
import com.jwbutler.krpg.players.KeyboardPlayer
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.graphics.GameRenderer
import com.jwbutler.rpglib.graphics.GameWindow
import com.jwbutler.rpglib.graphics.awt.GameRendererAWT
import com.jwbutler.rpglib.graphics.awt.GameWindowAWT
import com.jwbutler.rpglib.graphics.awt.ImageLoaderAWT
import com.jwbutler.rpglib.graphics.images.Colors
import com.jwbutler.rpglib.graphics.images.ImageLoader
import com.jwbutler.rpglib.graphics.images.PaletteSwaps
import com.jwbutler.rpglib.sounds.SoundPlayer

fun main()
{
    val gameView = GameView.initialize { KeyboardGameView() }
    ImageLoader.initialize { ImageLoaderAWT { filename -> "/png/${filename}.png" } }
    val state = GameState.initialize()
    GameWindow.initialize { GameWindowAWT(gameView.initialWindowDimensions) }
    GameRenderer.initialize { GameRendererAWT(gameView.gameDimensions) }
    val engine = GameEngine.initialize()
    SoundPlayer.initialize { SoundPlayer { filename -> "/sounds/${filename}.wav" } }

    val humanPlayer = KeyboardPlayer()
    state.addPlayer(humanPlayer)
    val enemyPlayer = EnemyPlayer()
    state.addPlayer(enemyPlayer)

    val level = LEVEL_ONE

    /*val level = BitmapLevelCreator.loadLevel(
        filename = "level0",
        baseTileType = TileType.STONE,
        victoryCondition = VictoryCondition.NONE
    )*/

    /*val level = LevelGenerator.create()
        .generate(Dimensions(40, 40), VictoryCondition.NONE)*/

    engine.startGame(level, _getInitialUnits())
}

private fun _getInitialUnits(): List<GameEngine.UnitData>
{
    val state = GameState.getInstance()
    val humanPlayer = state.getHumanPlayer()

    val paletteSwaps = PaletteSwaps.WHITE_TRANSPARENT
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
        )
    )
}