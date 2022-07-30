package ru.laifada.spleef.event

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.inventory.ItemStack
import ru.laifada.spleef.Spleef

object Listeners : Listener {

    @EventHandler
    fun playerJoinTurn(event: PlayerInteractEvent) {
        if (event.item?.type != Material.STONE_SHOVEL) return

        if (Spleef.instance?.gameBrain?.turn?.hasPlayerInTurn(event.player) == false) {
            event.player.sendMessage("Вы встали в очередь!")
            Spleef.instance?.gameBrain?.addPlayerInTurn(event.player, 2)
        } else {
            event.player.sendMessage("Вы уже стоите в очереди!")
        }
    }

    @EventHandler
    fun playerDamage(event : EntityDamageEvent) {
        if (event.entity.type != EntityType.PLAYER) return
        event.isCancelled = true
    }

    @EventHandler
    fun playerJoinServer(event: PlayerJoinEvent) {
        val player: Player = event.player
        player.inventory.clear()
        player.inventory.addItem(ItemStack(Material.STONE_SHOVEL))
        event.player.teleport(Spleef.instance?.lobby!!)
    }

    @EventHandler
    fun playerLeaveServer(event: PlayerQuitEvent) {
        Spleef.instance?.gameBrain?.turn?.removePlayer(event.player)
    }

    @EventHandler
    fun playerFood(event: FoodLevelChangeEvent) {
        event.foodLevel = 20
    }

    @EventHandler
    fun playerAddInTurn(event: PlayerAddInTurn) {
        Spleef.instance?.gameBrain?.addPlayerInTurn(event.player, event.size)
    }

    @EventHandler
    fun arenaEnd(event: ArenaEnd) {
        Spleef.instance?.gameBrain?.checkStartArena(event.arena.size)
    }
}