package ru.laifada.spleef.brain

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import ru.laifada.spleef.arena.Arena
import ru.laifada.spleef.status.ArenaStatus

class GameBrain {

    val arenas : ArrayList<Arena> = ArrayList()
    val turn : Turn = Turn()

    constructor() {
        generateArenas()
    }

    fun addPlayerInTurn(_player: Player, _count: Int = 2) {
        turn.addPlayerTurn(_player, _count)
        Bukkit.getConsoleSender().sendMessage("Player ${_player.name} add in turn")

        checkStartArena(_count)
    }

    fun checkStartArena(_count: Int) {
        if (turn.size(_count)!! >= _count) {
            val arena: Arena? = findArena()

            for (i in 0 until _count) {
                val player: Player? = turn.getPlayer(_count)
                arena?.addPlayer(player)
                Bukkit.getConsoleSender().sendMessage("Player ${player?.name} add in arena: ${arena?.name}")
            }
        }
    }

    fun findArena() : Arena? {

        for (arena in arenas) {
            if (arena.status == ArenaStatus.WAIT) return arena
        }

        return null
    }

    fun generateArenas() {
        var arena = Arena("ARENA1", Location(Bukkit.getWorld("world"), 54.0, 112.0, -20.0), Location(Bukkit.getWorld("world"), 66.0, 112.0, -2.0))
        arena.addSpawnLocation(56.5, 113.0, -4.5, -150f, 0f)
        arena.addSpawnLocation(64.5, 113.0, -18.5, 30f, 0f)
        arenas.add(arena)

        arena = Arena("ARENA2", Location(Bukkit.getWorld("world"), 31.0, 112.0, -37.0), Location(Bukkit.getWorld("world"), 49.0, 112.0, -25.0))
        arena.addSpawnLocation(47.5, 113.0, -27.5, 120f, 0f)
        arena.addSpawnLocation(33.5, 113.0, -35.5, -60f, 0f)
        arenas.add(arena)
    }

}