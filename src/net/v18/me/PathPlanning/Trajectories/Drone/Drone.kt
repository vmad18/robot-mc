package net.v18.me.PathPlanning.Trajectories.Drone

import net.v18.me.PathPlanning.Trajectories.Trajectory
import net.v18.me.robot_utils.Orientation
import net.v18.me.utils.TrajUtil
import net.v18.me.utils.graph.Node
import net.v18.me.utils.math.Vec3D
import net.v18.me.utils.math.Vector
import net.v18.me.utils.todeg
import net.v18.me.utils.zero3D
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 *
 * @constructor
 * @param m - mass of the drone
 * @param l - length of the drone
 * @param mc - motor constant
 * @param inertia - moment of inertia
 * @param g - gravitational constant
 */
class Drone(m: Number, l: Number, mc: Number, val inertia: Vec3D, var g: Vec3D = Vec3D(0, 0, -9.81)): Trajectory {

    override val constraints: HashMap<TrajUtil, Pair<(Double, Double, Double) -> Boolean, (Double) -> Vec3D>> = hashMapOf()

    val mass: Double = m.toDouble()

    val length: Double = l.toDouble()

    val motorc = mc.toDouble()

    val pos: Vec3D = zero3D

    val pos_dot: Vec3D = zero3D

    var pos_ddot: Vec3D = zero3D

    var vel: Vec3D = zero3D

    val angular: Vec3D = zero3D

    val ang: Vec3D = zero3D

    var ang_dot: Vec3D = zero3D

    val thrust_v: Vector = Vector(0, 0, 0, 0)

    val thrust: Orientation = Orientation(Vec3D(0, 0, 1))

    fun gen_thrust(vec: Vector){
        for(i: Int in 0 until vec.space) thrust_v[i] = this.motorc*vec[i]*vec[i]
    }

    val thrust_z: Unit
        get(){
            var thrust_t: Double = 0.0
            for(i: Int in 0 until thrust_v.space) thrust_t += thrust_v[i]
            thrust.pose = thrust.pose * -thrust_t
        }

    val torques: Vec3D
        get() = Vec3D(length * (thrust_v[3] - thrust_v[1])/inertia.x, length * (thrust_v[2] - thrust_v[0])/inertia.y, length * (thrust_v[0] + thrust_v[2] - thrust_v[1] - thrust_v[3])/inertia.z)

    override fun move(dt: Number, vararg w: Number): Node {

        assert(w.size == 4)

        gen_thrust(Vector(*w))

        thrust_z
        println("thrust ${thrust.pose.vec}")

        for(steps:Int in 0..6000) {

            val angular_dot = torques + (Vec3D(
                angular.y * angular.z * ((inertia.y - inertia.z) / inertia.x),
                angular.x * angular.z * ((inertia.z - inertia.x) / inertia.y),
                angular.x * angular.y * ((inertia.x - inertia.y) / inertia.z)
            ) * -1)

            //println("YEEE ${angular.x * angular.y * ((inertia.x - inertia.y) / inertia.z)}")
            //println("torque: ${torques} ${angular}")
            //println("angular ${(inertia.x - inertia.y)} ${inertia.z} ${(inertia.x)} ${(inertia.x - inertia.y) / inertia.z}")
            //println("angular_dot ${angular_dot}")
            angular += angular_dot*dt

            ang_dot = Vec3D(
                angular.x + (angular.y * sin(ang.x) + angular.z * cos(ang.x))*tan(ang.y),
                angular.y * cos(ang.x) - angular.z * sin(ang.x),
                (angular.y * sin(ang.x) + angular.z * cos(ang.x)) * 1/cos(ang.y)
            )

            println("angle dot $ang_dot")

            ang += ang_dot*dt

            println("angles $ang ${((ang.x.todeg) % 360 + 360)%360} ${((ang.y.todeg) % 360 + 360)%360} ${((ang.z.todeg) % 360 + 360)%360}")

            pos_ddot = g + (thrust.rotate(ang.z, ang.y, ang.x).vec * (-1.0/mass))
            println("pos ddot $pos_ddot")
            thrust.initial
            pos_dot += pos_ddot*dt
            println("pos_dot $pos_dot")
            pos += pos_dot*dt
            println("pos $pos")

            vel = Orientation(pos_dot.copy).r_rotate(ang.z, ang.y, ang.x).vec * thrust.pose.mag
            println("velocity $vel ${thrust.pose.mag}")
        }

        TODO("Not yet implemented")
    }
}

fun main() {
    var d: Drone = Drone(.456, 2, .5, Vec3D(4.856e-3, 4.856e-3, 8.801e-3))
    d.move(.005, 2, 2, 1, 1)
}