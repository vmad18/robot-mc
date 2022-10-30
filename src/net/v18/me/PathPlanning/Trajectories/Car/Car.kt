package net.v18.me.PathPlanning.Trajectories.Car

import net.v18.me.PathPlanning.Trajectories.Trajectory
import net.v18.me.utils.TrajUtil
import net.v18.me.utils.graph.Node
import net.v18.me.utils.math.Vec3D
import kotlin.math.*

class Car(private var l: Number): Trajectory {
    
    override val constraints: HashMap<TrajUtil, Pair<(Double, Double, Double) -> Boolean, (Double) -> Vec3D>> = hashMapOf()

    init {
        constraints[TrajUtil.VEL] = Pair({ x: Double, y: Double, z: Double -> true }, { Vec3D(cos(it), sin(it), 0) })
        constraints[TrajUtil.ANGLES] = Pair({theta: Double, phi: Double, psi: Double -> abs(theta) < PI/4 }, { Vec3D(tan(it), 0, 0) })
    }

    override fun move(dt: Number, vararg s: Number): Node {
        TODO("Not yet implemented")
    }

}