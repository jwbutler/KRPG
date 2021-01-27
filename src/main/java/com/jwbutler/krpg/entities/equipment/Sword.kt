package com.jwbutler.krpg.entities.equipment

import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.graphics.sprites.SwordSprite

class Sword : AbstractEquipment(SwordSprite(PaletteSwaps().withTransparentColor(Colors.WHITE)))
{
    override val slot = RPGEquipmentSlot.MAIN_HAND
}