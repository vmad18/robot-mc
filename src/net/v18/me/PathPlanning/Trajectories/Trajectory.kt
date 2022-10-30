package net.v18.me.PathPlanning.Trajectories

import net.v18.me.utils.TrajUtil
import net.v18.me.utils.graph.Node
import net.v18.me.utils.math.Vec3D

interface Trajectory {

    val constraints: HashMap<TrajUtil, Pair<(Double, Double, Double) -> Boolean, (Double) -> Vec3D>>

    /**
     * Dynamic model
     * @param dt - time step
     * @param m - magnitudes
     */
    fun move(dt: Number, vararg m: Number): Node

}