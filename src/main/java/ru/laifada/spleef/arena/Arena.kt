package ru.laifada.spleef.arena

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.inventory.ItemStack
import ru.laifada.spleef.Spleef
import ru.laifada.spleef.status.ArenaStatus
import java.lang.Double.max
import java.lang.Double.min


class Arena(_name: String, val locSnow1: Location, val locSnow2: Location) : Listener {

    var playersInGame : ArrayList<Player?> = ArrayList()
    var playersDeath : ArrayList<Player> = ArrayList()
    var status : ArenaStatus = ArenaStatus.WAIT
    var spawnLocation: ArrayList<Location> = ArrayList()
    var size: Int = spawnLocation.size
    var name = _name

    fun addPlayer(player : Player?) : Boolean {
        if (status != ArenaStatus.WAIT) return false;
        playersInGame.add(player)
        if (playersInGame.size == size) {
            startGame()
        }

        return true
    }

    fun startGame() {
        status = ArenaStatus.GAME
        Bukkit.getConsoleSender().sendMessage("START ARENA")
        for ((i, player) in playersInGame.withIndex()) {
            player?.teleport(spawnLocation[i])
            player?.gameMode = GameMode.SURVIVAL
            player?.sendMessage("Игра начата! игроков: ${playersInGame.size}")

            player?.inventory?.clear()
            player?.inventory?.addItem(ItemStack(Material.DIAMOND_SHOVEL))
        }
    }

    fun playerLose(player : Player) {
        player.gameMode = GameMode.SPECTATOR
        player.sendMessage("Ты проиграл.")

        playersDeath.add(player)
        playersInGame.remove(player)

        if (checkEndgame()) {
            gameEnd()
        }
    }

    fun checkEndgame() : Boolean {
        if (playersInGame.size == 1) {
            playerWin(playersInGame[0])
            return true
        }

        return false
    }

    fun playerWin(player: Player?) {
        player?.sendMessage("Ты победил.")
    }

    fun gameEnd() {
        for (player in playersInGame) {
            returnLobby(player)
        }
        for (player in playersDeath) {
            returnLobby(player)
        }

        playersInGame = ArrayList()
        playersDeath = ArrayList()
        status = ArenaStatus.WAIT
        fixArena()
    }

    fun addSpawnLocation(x: Double, y: Double, z: Double, pitch: Float = 0f, yaw: Float = 0f) {
        spawnLocation.add(Location(Bukkit.getWorld("world"), x, y, z, pitch, yaw))
        size = spawnLocation.size
    }

    private fun returnLobby(player : Player?) {
        player?.teleport(Spleef.instance?.lobby!!)
        player?.gameMode = GameMode.ADVENTURE
        player?.inventory?.clear()
        player?.inventory?.addItem(ItemStack(Material.STONE_SHOVEL))
    }

    private fun fixArena() {
        var startX = min(locSnow1.x, locSnow2.x)
        var endX = max(locSnow1.x, locSnow2.x)

        var startZ = min(locSnow1.z, locSnow2.z)
        var endZ = max(locSnow1.z, locSnow2.z)

        for (x in startX.toInt() until endX.toInt()+1) {
            for (z in startZ.toInt() until endZ.toInt()+1) {
                Location(Bukkit.getWorld("world"), x.toDouble(), locSnow1.y, z.toDouble()).block.type = Material.SNOW_BLOCK
            }
        }
    }

    @EventHandler
    fun playerLose(event: PlayerMoveEvent) {
        if (!playersInGame.contains(event.player)) return

        if (event.player.location.y < locSnow1.y - 4) {
            playerLose(event.player)
        }
    }

    @EventHandler
    fun playerBreakBlock(event: BlockBreakEvent) {
        if (event.block.type == Material.SNOW_BLOCK) {
            event.isCancelled = true
            event.block.type = Material.AIR
            return
        }
        event.isCancelled = true
    }
}
