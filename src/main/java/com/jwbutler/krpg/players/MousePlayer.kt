package com.jwbutler.krpg.players

import com.jwbutler.gameengine.geometry.Pixel
import com.jwbutler.gameengine.graphics.GameWindow
import com.jwbutler.gameengine.input.KeyboardKey
import com.jwbutler.gameengine.input.KeyboardListener
import com.jwbutler.gameengine.input.ModifierKey
import com.jwbutler.gameengine.input.MouseButton
import com.jwbutler.gameengine.input.MouseListener
import com.jwbutler.krpg.behavior.RPGActivity
import com.jwbutler.krpg.behavior.commands.BashCommand
import com.jwbutler.krpg.behavior.commands.Command
import com.jwbutler.krpg.behavior.commands.CommandType
import com.jwbutler.krpg.behavior.commands.MoveCommand
import com.jwbutler.krpg.behavior.commands.RepeatingAttackCommand
import com.jwbutler.krpg.behavior.commands.StayCommand
import com.jwbutler.krpg.core.GameEngine
import com.jwbutler.krpg.core.RPGGameView
import com.jwbutler.krpg.core.Singletons
import com.jwbutler.krpg.utils.getAverageCoordinates
import com.jwbutler.krpg.utils.getPlayerUnits
import com.jwbutler.krpg.utils.getUnitsInPixelRect
import com.jwbutler.krpg.utils.rectFromPixels
import com.jwbutler.rpglib.behavior.Activity
import com.jwbutler.rpglib.core.GameState
import com.jwbutler.rpglib.core.GameView
import com.jwbutler.rpglib.entities.units.Unit
import com.jwbutler.rpglib.geometry.Coordinates
import com.jwbutler.rpglib.geometry.Direction
import com.jwbutler.rpglib.geometry.pixelToCoordinates
import com.jwbutler.rpglib.players.HumanPlayer

private typealias CommandSupplier = (Unit) -> Command

class MousePlayer : HumanPlayer()
{
    private val currentCommands = mutableMapOf<Unit, Command>()
    private val queuedCommands = mutableMapOf<Unit, CommandSupplier>()
    private val selectedUnits = mutableSetOf<Unit>()

    override fun chooseActivity(unit: Unit): Pair<Activity, Direction>
    {
        val currentCommand = currentCommands[unit] ?: StayCommand(unit)
        val nextCommand = _chooseCommand(unit)
        val command: Command
        if (currentCommand.isComplete())
        {
            command = nextCommand
        }
        // TODO: Replace reference to hardcoded command type
        else if (currentCommand.isPreemptible() && nextCommand.type != CommandType.STAY)
        {
            command = nextCommand
        }
        else
        {
            command = currentCommand
        }
        currentCommands[unit] = command
        return command.chooseActivity()
    }

    fun getSelectedUnits() = selectedUnits

    override fun getKeyListener() = object : KeyboardListener
    {
        override fun keyUp(key: KeyboardKey, modifiers: Set<ModifierKey>)
        {
            when (key)
            {
                KeyboardKey.SPACE -> GameEngine.getInstance().togglePause()
                KeyboardKey.ENTER ->
                {
                    if (modifiers.contains(ModifierKey.ALT))
                    {
                        Singletons.get(GameWindow::class.java).toggleMaximized()
                    }
                }
                KeyboardKey.A ->
                {
                    if (modifiers.contains(ModifierKey.CTRL))
                    {
                        selectedUnits.clear()
                        selectedUnits.addAll(getPlayerUnits())
                    }
                }
                KeyboardKey.KEY_1, KeyboardKey.KEY_2, KeyboardKey.KEY_3, KeyboardKey.KEY_4, KeyboardKey.KEY_5 ->
                {
                    _handleNumberKey(key, modifiers)
                }
                KeyboardKey.UP, KeyboardKey.DOWN, KeyboardKey.LEFT, KeyboardKey.RIGHT ->
                {
                    _handleMoveCamera(key, modifiers)
                }
                KeyboardKey.W ->
                {
                    if (modifiers.contains(ModifierKey.CTRL))
                    {
                        GameState.getInstance().getLevel().forceVictory = true
                    }
                }
                KeyboardKey.C ->
                {
                    if (selectedUnits.isNotEmpty())
                    {
                        val cameraCoordinates = getAverageCoordinates(selectedUnits.map(Unit::getCoordinates))
                        val gameView = GameView.getInstance() as RPGGameView
                        gameView.setCameraCoordinates(cameraCoordinates)
                    }
                }
            }
        }

        private fun _handleNumberKey(key: KeyboardKey, modifiers: Set<ModifierKey>)
        {
            val i = when(key)
            {
                KeyboardKey.KEY_1 -> 1
                KeyboardKey.KEY_2 -> 2
                KeyboardKey.KEY_3 -> 3
                KeyboardKey.KEY_4 -> 4
                KeyboardKey.KEY_5 -> 5
                else -> throw RuntimeException()
            }
            val playerUnits = getPlayerUnits()
            if (playerUnits.lastIndex >= i)
            {
                if (modifiers.contains(ModifierKey.CTRL))
                {
                    val unit = playerUnits[i]
                    if (selectedUnits.contains(unit))
                    {
                        selectedUnits.remove(unit)
                    }
                    else
                    {
                        selectedUnits.add(unit)
                    }
                }
                else
                {
                    selectedUnits.clear()
                    selectedUnits.add(playerUnits[i])
                }
            }
        }

        private fun _handleMoveCamera(key: KeyboardKey, modifiers: Set<ModifierKey>)
        {
            val view = GameView.getInstance() as RPGGameView
            val cameraCoordinates = view.getCameraCoordinates()
            val (x, y) = cameraCoordinates

            var (dx, dy) = Pair(0, 0)

            when (key)
            {
                KeyboardKey.UP    -> dy--
                KeyboardKey.DOWN  -> dy++
                KeyboardKey.LEFT  -> dx--
                KeyboardKey.RIGHT -> dx++
                else -> {}
            }

            val newCoordinates = Coordinates(x + dx, y + dy)
            if (GameState.getInstance().containsCoordinates(newCoordinates))
            {
                view.setCameraCoordinates(newCoordinates)
            }
        }
    }

    override fun getMouseListener() = object : MouseListener
    {
        override fun mouseDown(pixel: Pixel, button: MouseButton, modifiers: Set<ModifierKey>)
        {
            if (_isLeftClick(button, modifiers))
            {
                _getView().selectionStart = pixel
            }
            else if (_isRightClick(button, modifiers))
            {
                val coordinates = pixelToCoordinates(pixel)
                if (GameState.getInstance().containsCoordinates(coordinates))
                {
                    for (unit in selectedUnits)
                    {
                        val queuedCommand: CommandSupplier

                        if (modifiers.contains(ModifierKey.CTRL))
                        {
                            queuedCommand = { u ->
                                _tryBash(u, coordinates)
                                    ?: _tryAttack(u, coordinates)
                                    ?: _tryMove(u, coordinates)
                                    ?: _stay(u, coordinates)
                            }
                        }
                        else
                        {
                            queuedCommand = { u ->
                                _tryAttack(u, coordinates)
                                    ?: _tryMove(u, coordinates)
                                    ?: _stay(u, coordinates)
                            }
                        }

                        queuedCommands[unit] = queuedCommand
                    }
                }
            }
        }

        override fun mouseDragged(pixel: Pixel, buttons: Set<MouseButton>)
        {
            if (buttons.contains(MouseButton.LEFT)) // TODO: check modifiers for consistency
            {
                _getView().selectionEnd = pixel
            }
        }

        override fun mouseUp(pixel: Pixel, button: MouseButton, modifiers: Set<ModifierKey>)
        {
            val view = _getView()
            if (_isLeftClick(button, modifiers))
            {
                if (view.selectionStart != null && view.selectionEnd != null)
                {
                    val selectionRect = rectFromPixels(view.selectionStart!!, view.selectionEnd!!)
                    selectedUnits.clear()
                    selectedUnits.addAll(
                        getUnitsInPixelRect(selectionRect).filter { u -> u.getPlayer().isHuman }
                    )
                }
                view.selectionStart = null
                view.selectionEnd = null
            }
        }

        override fun mouseClicked(pixel: Pixel, button: MouseButton, modifiers: Set<ModifierKey>)
        {
            val view = _getView()
            if (_isLeftClick(button, modifiers))
            {
                view.selectionStart = null
                view.selectionEnd = null
                selectedUnits.clear()
            }
        }

        private fun _isLeftClick(button: MouseButton, modifiers: Set<ModifierKey>): Boolean
        {
            return button == MouseButton.LEFT && !modifiers.contains(ModifierKey.CTRL)
        }

        private fun _isRightClick(button: MouseButton, modifiers: Set<ModifierKey>): Boolean
        {
            return (button == MouseButton.RIGHT)
                || (button == MouseButton.LEFT && modifiers.contains(ModifierKey.CTRL))
        }
    }

    private fun _chooseCommand(unit: Unit): Command
    {
        val queuedCommand = queuedCommands.remove(unit)
        if (queuedCommand != null)
        {
            return queuedCommand(unit)
        }
        else
        {
            return StayCommand(unit)
        }
    }

    companion object
    {
        private fun _tryAttack(unit: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != unit.getCoordinates())
            {
                if (unit.isActivityReady(RPGActivity.ATTACKING))
                {
                    val targetUnit = GameState.getInstance().getUnit(coordinates)
                    if (targetUnit != null && !(targetUnit.getPlayer() is HumanPlayer))
                    {
                        return RepeatingAttackCommand(unit, targetUnit)
                    }
                }
            }
            return null
        }

        private fun _tryBash(unit: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != unit.getCoordinates())
            {
                if (unit.isActivityReady(RPGActivity.BASHING))
                {
                    val targetUnit = GameState.getInstance().getUnit(coordinates)
                    if (targetUnit != null && !(targetUnit.getPlayer() is HumanPlayer))
                    {
                        return BashCommand(unit, targetUnit)
                    }
                }
            }
            return null
        }

        private fun _tryMove(unit: Unit, coordinates: Coordinates): Command?
        {
            if (coordinates != unit.getCoordinates())
            {
                return MoveCommand(unit, coordinates)
            }
            return null
        }

        private fun _stay(unit: Unit, coordinates: Coordinates) = StayCommand(unit)
    }

    /**
     * TODO well this is a piece of shit
     */
    fun getCommand(unit: Unit): Command
    {
        return queuedCommands[unit]?.invoke(unit)
            ?: currentCommands[unit]
            ?: StayCommand(unit) // TODO - can we just initialize currentCommands with some of these?
    }

    private fun _getView(): RPGGameView = GameView.getInstance() as RPGGameView
}