package net.v18.me.utils

import net.v18.me.utils.math.Vec3D
import org.bukkit.Location
import net.v18.me.utils.math.Vector
import kotlin.math.PI

val Number.todeg: Double
    get() = this.toDouble() * 180/PI

val Number.torad: Double
    get() = this.toDouble() * PI/180

val Location.toVec3D: Vec3D
    get() = Vec3D(this.x, this.y, this.z)

val Array<Number>.toVec3D: Vec3D
    get() = when(this.size){
            0 -> Vec3D(0, 0, 0)
            1 -> Vec3D(this[0], 0, 0)
            2 -> Vec3D(this[0], this[1], 0)
            else -> Vec3D(this[0], this[1], this[2])
        }

val Vec3D.zero: Vec3D
    get() = Vec3D(0.0, 0.0, 0.0)

val zero3D: Vec3D
    get() = Vec3D(0, 0, 0)

operator fun Number.times(v: Vec3D): Vec3D = v*this


/*fun zerod(s: Int): Vector<Double> {
    var vec: ArrayList<Double> = ArrayList()
    for(i: Int in 0 until s) vec[i] = 0.0
    return Vector(*vec.toTypedArray())
}

val Vector<Number>.td: Vector<Double>
    get(){
        var vec: Vector<Double> = zerod(this.space)
        for(i:Int in 0 until this.space)  vec[i] = this[i].toDouble()
        return vec
    }*/