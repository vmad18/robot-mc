package net.v18.me.utils.math

import org.bukkit.Location
import org.bukkit.World
import kotlin.math.round
import kotlin.math.sqrt

class Vec3D(x:Number, y:Number, z:Number) {

    var x: Double = x.toDouble()

    var y: Double = y.toDouble()

    var z: Double = z.toDouble()

    val mag: Double
        get() = sqrt(x * x + y * y + z * z)

    val norm: Vec3D
        get() = this * (1.0/mag)

    val reverse: Vec3D
        get() = Vec3D(z, y, x)

    val quant: Quaternion
        get() = Quaternion(this)

    val vx: Vec3D
        get() = Vec3D(x, 0, 0)

    val vy: Vec3D
        get() = Vec3D(0, y, 0)

    val vz: Vec3D
        get() = Vec3D(0, 0, z)

    val copy: Vec3D
        get() = Vec3D(x, y, z)

    override fun toString(): String = "${round(1000*x)/1000} ${round(1000*y)/1000} ${round(z*1000)/1000}"


    fun as_loc(w: World): Location = Location(w, x, y, z)

    operator fun plus(v: Vec3D): Vec3D = Vec3D(x+v.x, y+v.y, z+v.z)
    operator fun times(n: Number): Vec3D = Vec3D(x*n.toDouble(), y*n.toDouble(), z*n.toDouble())
    operator fun plusAssign(v: Vec3D): Unit {
        this.x += v.x
        this.y += v.y
        this.z += v.z
    }

}