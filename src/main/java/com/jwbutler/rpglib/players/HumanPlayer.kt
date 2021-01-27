package com.jwbutler.rpglib.players

import com.jwbutler.gameengine.graphics.GameWindow
import com.jwbutler.gameengine.input.KeyboardListener
import com.jwbutler.gameengine.input.MouseListener
import com.jwbutler.krpg.core.Singletons

abstract class HumanPlayer : AbstractPlayer()
{
    final override val isHuman = true

    init
    {
        // IntelliJ doesn't like this but it seems ok
        val gameWindow = Singletons.get(GameWindow::class.java)
        gameWindow.setKeyboardListener(getKeyListener())
        gameWindow.setMouseListener(getMouseListener())
    }

    open fun getKeyListener(): KeyboardListener = object : KeyboardListener {}
    open fun getMouseListener(): MouseListener = object : MouseListener {}
}