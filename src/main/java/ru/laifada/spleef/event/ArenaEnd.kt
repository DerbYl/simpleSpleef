package ru.laifada.spleef.event

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import ru.laifada.spleef.arena.Arena

class ArenaEnd(_arena: Arena) : Event() {
    companion object {
        @JvmStatic
        val handlerList = HandlerList()
    }

    override fun getHandlers(): HandlerList = handlerList

    val arena : Arena = _arena
}