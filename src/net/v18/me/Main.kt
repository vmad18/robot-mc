package net.v18.me

import net.v18.me.PathPlanning.RRTStar
import net.v18.me.robot_utils.Robot
import net.v18.me.drone.Drone
import net.v18.me.events.DoRandomEvent
import net.v18.me.utils.tell
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author v18
 * first spigot plugin in kotlin =D
 **/

class Main: JavaPlugin(), CommandExecutor {

    private var bots = hashMapOf<String, Robot>()

    private var l1: Location? = null

    private var l2: Location? = null

    override fun onEnable(){
        this.server.pluginManager.registerEvents(DoRandomEvent(this), this)
    }

    override fun onDisable(){}

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) return false

        var p:Player = sender

        return when(label.lowercase()){
            "drone" -> {
                var drone: Drone = Drone(this, p.location)
                if(args.isNotEmpty()){
                    bots[args[0]] = drone
                    Bukkit.broadcastMessage("Sup frickers")
                }
                return true
            }
            "wp" -> {
                //lazy implementation
                bots.forEach { it.value.waypoints.add(p.location.clone().add(0.0, -1.0, 0.0)) };
                p.location.clone().add(0.0, -1.0, 0.0).block.type = Material.EMERALD_BLOCK
                return true
            }
            "traverse" -> {
                bots.forEach{
                    if(it.value is Drone){
                        (it.value as Drone).traverse;
                    }
                }
                return true
            }
            "pos" -> {
                if(l1 == null){
                    l1 = p.location.clone().add(0.0, -1.0, 0.0)
                    tell(p, "${ChatColor.RED} starting point set!")
                }else if(l2 == null){
                    l2 = p.location.clone().add(0.0, -1.0, 0.0)
                    tell(p, "${ChatColor.RED} goal point set!")
                }else{
                    val rrt_star = RRTStar(this, bots[bots.keys.first()]!!, l1!!, l2!!)
                    tell(p, "${ChatColor.DARK_AQUA} starting RRT*")
                    rrt_star.run
                    l1 = null
                    l2 = null
                }
                return true
            }
            else -> return false
        }
    }

}