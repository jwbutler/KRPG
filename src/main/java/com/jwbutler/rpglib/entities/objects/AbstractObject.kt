package com.jwbutler.rpglib.entities.objects

import com.jwbutler.rpglib.core.GameState

abstract class AbstractObject : GameObject
{
    override fun getCoordinates() = GameState.getInstance().getCoordinates(this)
    override fun exists() = GameState.getInstance().containsEntity(this)
    override fun render() = sprite.render(this)

    override fun update() {}
}