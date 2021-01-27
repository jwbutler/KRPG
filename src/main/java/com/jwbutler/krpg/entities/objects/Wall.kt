package com.jwbutler.krpg.entities.objects

import com.jwbutler.gameengine.graphics.Image
import com.jwbutler.gameengine.graphics.ImageLoader
import com.jwbutler.krpg.core.Singletons
import com.jwbutler.rpglib.entities.objects.AbstractObject
import com.jwbutler.rpglib.graphics.RenderLayer
import com.jwbutler.rpglib.graphics.sprites.StaticSprite

class Wall : AbstractObject()
{
    override val sprite = StaticSprite(_getImage(), RenderLayer.OBJECT)
    override fun isBlocking() = true

    companion object
    {
        private fun _getImage(): Image
        {
            return Singletons.get(ImageLoader::class.java).loadImage("objects/wall", null)
        }
    }
}