package net.v18.me.robot_utils

import net.v18.me.utils.math.Axes
import net.v18.me.utils.math.Quaternion
import net.v18.me.utils.math.RotateVector
import net.v18.me.utils.math.Vec3D

class Orientation(inp: Vec3D) {

    var pose: Quaternion = Quaternion(inp.copy.norm)

    private var theta: Double = 0.0

    private var phi: Double = 0.0

    private var psi: Double = 0.0

    private var total_rots: Quaternion? = null

    val initial: Unit
        get(){
            pose = (total_rots!!.conj * pose) * total_rots!!
            total_rots = null
        }

    /**
     * Rotation follows Z-Y-X
     *
     * @param t - theta yaw
     * @param ph - phi pitch
     * @param ps - psi roll
     **/

    fun rotate(t: Number=0, ph: Number=0, ps: Number=0): Quaternion {
        theta += t.toDouble()
        phi += ph.toDouble()
        psi += ps.toDouble()

        var r = Quaternion(Axes.Z.v, t)*Quaternion(Axes.Y.v, ph)*Quaternion(Axes.X.v, ps)
        total_rots = if(total_rots != null) r*total_rots!! else r
        val hp:Quaternion = r*pose
        pose = (hp*r.conj)
        return pose
    }

    fun r_rotate(t: Number=0, ph: Number=0, ps: Number=0): Quaternion {
        var r = Quaternion(Axes.Z.v, t)*Quaternion(Axes.Y.v, ph)*Quaternion(Axes.X.v, ps).conj.norm
        pose *= r
        pose = r.conj*pose
        return pose
    }

}


/**
 *
var rot = RotateVector(theta, Axes.X.v, pose.vec)
println(rot)
rot = RotateVector(phi, Axes.Y.v, rot.vec)
rot = RotateVector(psi, Axes.Z.v, rot.vec)
pose = rot

return pose
 *
 */