package com.jwbutler.krpg.behavior.commands

import com.jwbutler.krpg.behavior.Activity
import com.jwbutler.krpg.core.Direction
import com.jwbutler.krpg.entities.units.Unit

class DieCommand(override val source: Unit) : Command
{
    override val type = CommandType.DIE
    private var hasFallen = false

    override fun chooseActivity(): Pair<Activity, Direction>
    {
        if (!hasFallen)
        {
            hasFallen = true
            return Pair(Activity.FALLING, source.getDirection())
        }
        else
        {
            error("Shouldn't get here!")
            return Pair(Activity.DEAD, source.getDirection())
        }
    }

    override fun isPreemptible() = false
    override fun isComplete() = false

    override fun toString(): String
    {
        return "DieCommand{}"
    }
}