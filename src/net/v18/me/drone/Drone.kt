package net.v18.me.drone

import net.v18.me.Main
import net.v18.me.PathPlanning.Trajectories.Car.Car
import net.v18.me.PathPlanning.Trajectories.Trajectory
import net.v18.me.robot_utils.Orientation
import net.v18.me.robot_utils.Robot
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.ArrayList
import org.bukkit.util.Vector
import net.v18.me.utils.*
import kotlin.math.atan2


class Drone(private val plugin: Main, loc: Location): Robot, Listener {

    private val drone: ArmorStand = loc.world!!.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand;

    private var home: Location = loc;

    init {
        drone.isSmall = true;
        drone.setGravity(false);
        //drone.isVisible = false;

        val skull = ItemStack(Material.PLAYER_HEAD);

        val skullmeta: SkullMeta = skull.itemMeta as SkullMeta;
        skullmeta.owningPlayer = Bukkit.getOfflinePlayer(UUID.fromString("ee64b618-240b-49c6-a89d-c14f3ba7ee11"));

        skull.itemMeta = skullmeta;

        drone.setHelmet(skull);

        animation;
    }

    override val orientation: Orientation = Orientation(home.toVec3D)

    override val constraints: Trajectory = Car(2)

    override val waypoints: ArrayList<Location> = ArrayList()

    val traverse:Unit
        get(){
            if(waypoints.isEmpty()) return

            drone.setGravity(true);
            object: BukkitRunnable(){
                var curr_goal:Location = waypoints[0]
                var index:Int = 0

                override fun run(){

                    var vel:Vector = curr_goal.clone().add(0.0, 1.0, 0.0).subtract(drone.location.clone()).toVector().normalize().multiply(.1);
                    drone.velocity = vel;

                    var dir = vel.clone()
                    //fuck this shit <3
                    val yaw:Double = atan2(curr_goal.toVector().normalize().z-drone.eyeLocation.direction.normalize().z, curr_goal.toVector().normalize().x-drone.eyeLocation.direction.normalize().x)
                        //acos(vel.clone().add(Vector(0, -vel.y, 0)).dot(drone.location.direction.clone())/(vel.length() * drone.location.direction.clone().length()));
                        // atan2(curr_goal.toVector().normalize().y-drone.location.direction.y, curr_goal.toVector().normalize().x-drone.location.direction.x)
                    drone.location.yaw = yaw.todeg.toFloat();

                    broad("${curr_goal.clone().add(0.0, 1.0, 0.0).distance(drone.location)} ${yaw.todeg} ${drone.location.yaw} ${(atan2(vel.z, vel.x).toFloat().todeg - 90)}")

                    if(curr_goal.clone().add(0.0, 1.0, 0.0).distance(drone.location) <= .5 || curr_goal.clone().distance(drone.location) <= .3){
                        drone.velocity = Vector(0, 0, 0)
                        index++;
                        if(index >= waypoints.size) cancel();

                        curr_goal = waypoints[index]
                    }

                }

            }.runTaskTimer(this.plugin, 0, 0)
        }


    override val animation:Unit
        get(){
            object: BukkitRunnable(){
                val end_time = System.currentTimeMillis() + 3*1000*60
                override fun run() {
                    drone.location.world!!.spawnParticle(Particle.SOUL_FIRE_FLAME, drone.location.clone().add(0.0, .75, 0.0), 0)
                    if(end_time <= System.currentTimeMillis()) cancel()
                }
            }.runTaskTimer(this.plugin, 1, 0)
        }
}