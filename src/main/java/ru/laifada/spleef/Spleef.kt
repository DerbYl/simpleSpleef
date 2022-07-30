package ru.laifada.spleef

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import ru.laifada.spleef.brain.GameBrain
import ru.laifada.spleef.event.Listeners

class Spleef : JavaPlugin() {

    companion object {
        var instance: Spleef? = null
            private set
        var plugin: Plugin? = null
            private set
    }

    val gameBrain = GameBrain()
    val lobby : Location = Location(Bukkit.getWorld("world"), 60.5, 113.0, -30.5, 45f, 0f)

    override fun onEnable() {
        instance = this
        plugin = this

        Bukkit.getServer().pluginManager.registerEvents(Listeners, this)

        for (arena in gameBrain.arenas) {
            Bukkit.getServer().pluginManager.registerEvents(arena, this)
        }

        for (i in Bukkit.getOnlinePlayers()) {
            i.teleport(lobby)
        }
    }
}