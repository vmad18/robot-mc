package net.v18.me.robot_utils

import net.v18.me.PathPlanning.Trajectories.Trajectory
import org.bukkit.Location

interface Robot {

    val orientation: Orientation

    val constraints: Trajectory

    val waypoints: ArrayList<Location>

    val animation: Unit
    
}