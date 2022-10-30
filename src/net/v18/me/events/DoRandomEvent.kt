package net.v18.me.events

import net.v18.me.Main
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemBreakEvent

class DoRandomEvent(private val plugin: Main): Listener {

    @EventHandler
    fun PlayerItemBreakEvent.dosomthin(){
        println("${this.player}")
    }

}