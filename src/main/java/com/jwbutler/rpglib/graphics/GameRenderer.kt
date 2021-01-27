package com.jwbutler.rpglib.graphics

import com.jwbutler.gameengine.geometry.Dimensions
import com.jwbutler.gameengine.graphics.Image
import com.jwbutler.rpglib.core.SingletonHolder

/**
 * This class renders the game at its "native" resolution.
 */
interface GameRenderer
{
    val dimensions: Dimensions

    fun render(): Image
    fun getImage(): Image

    companion object : SingletonHolder<GameRenderer>()
}