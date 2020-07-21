package com.jwbutler.krpg.graphics

import java.awt.Color

class PaletteSwaps(delegate: Map<Color, Color>)
{
    constructor() : this(mutableMapOf())

    private val delegate = delegate.toMutableMap()

    fun forEach(action: (Color, Color) -> Unit)
    {
        delegate.forEach(action)
    }

    fun withTransparentColor(color: Color): PaletteSwaps
    {
        val copy = delegate.toMutableMap()
        copy[color] = Colors.TRANSPARENT
        return PaletteSwaps(copy)
    }

    fun put(first: Color, second: Color): PaletteSwaps
    {
        return put(mapOf(first to second))
    }

    fun put(map: Map<Color, Color>): PaletteSwaps
    {
        val copy = delegate.toMutableMap()
        copy.putAll(map)
        return PaletteSwaps(copy)
    }

    companion object
    {
        val WHITE_TRANSPARENT = PaletteSwaps(mutableMapOf(Colors.WHITE to Colors.TRANSPARENT))
        val BLACK_TRANSPARENT = PaletteSwaps(mutableMapOf(Colors.BLACK to Colors.TRANSPARENT))
    }
}