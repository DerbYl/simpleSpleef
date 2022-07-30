package ru.laifada.spleef.brain

import org.bukkit.entity.Player

class Turn {
    private val turn: HashMap<Int, ArrayList<Player>> = HashMap()

    fun size(count: Int) : Int? {
        return turn.get(count)?.size
    }

    fun addPlayerTurn(player: Player, count: Int) : Boolean {
        for (p in turn.values) { // player now in turn?
            if (p.contains(player)) return false
        }

        if (!turn.containsKey(count)) turn.put(count, ArrayList()) // add turn player count

        val array = turn.get(count) // add in turn
        array?.add(player)
        array?.let { turn.put(count, it) }

        return false
    }

    fun removePlayer(player : Player) : Boolean {
        for (i in turn.values) {
            if (i.contains(player)) {
                i.remove(player)
                return true
            }
        }

        return false
    }

    fun getPlayer(count: Int) : Player? {
        val player: Player? = turn.get(count)?.get(0)
        turn.get(count)?.remove(player)
        return player
    }

    fun hasPlayerInTurn(player: Player) : Boolean {
        for (i in turn.values) {
            if (i.contains(player)) {
                return true
            }
        }

        return false
    }
}