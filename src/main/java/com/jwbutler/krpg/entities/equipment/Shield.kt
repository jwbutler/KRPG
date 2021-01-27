package com.jwbutler.krpg.entities.equipment

import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.graphics.sprites.ShieldSprite

class Shield : AbstractEquipment(ShieldSprite(PaletteSwaps().withTransparentColor(Colors.WHITE)))
{
    override val slot = RPGEquipmentSlot.OFF_HAND
}