package com.jwbutler.krpg.entities.equipment

import com.jwbutler.gameengine.graphics.PaletteSwaps
import com.jwbutler.krpg.graphics.Colors
import com.jwbutler.krpg.graphics.sprites.MailArmorSprite

private val PALETTE_SWAPS = PaletteSwaps().withTransparentColor(Colors.WHITE)

class MailArmor : AbstractEquipment(MailArmorSprite(PALETTE_SWAPS))
{
    override val slot = RPGEquipmentSlot.CHEST
}