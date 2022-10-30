package net.v18.me.utils.math

import net.v18.me.utils.zero
import org.bukkit.Location
import org.bukkit.util.Vector


enum class Axes(val v: Vec3D) {
    X(Vec3D(1, 0, 0)),
    Y(Vec3D(0, 1, 0)),
    Z(Vec3D(0, 0, 1))
}

data class EulerAngles(val yaw:Number, val pitch:Number, val roll:Number)

/**
 * @param angle - angle in radians to rotate around the axis
 * @param axis - a unit vector, e.g., x (1, 0, 0), y (0, 1, 0), z (0, 0, 1)
 * @param vec - unit vector to rotate around the axis
 **/
fun RotateVector(angle:Number, axis:Vec3D, vec:Vec3D): Quaternion{
    val q: Quaternion = Quaternion(axis.norm, angle) // rotation quaternion
    val r: Quaternion = vec.norm.quant // vector to quaternion

    val hp: Quaternion = q*r
    var final: Quaternion = hp*q.conj
    return final
}

fun line(loc1: Location, loc2: Location, parts:Int=1): ArrayList<Location>{
    val locs:ArrayList<Location> = ArrayList()

    val dir: Vector = loc2.clone().subtract(loc1.clone()).toVector().normalize().multiply(1/parts.toDouble())
    val dist: Double = loc2.distance(loc1) * parts

    val curr: Vector = Vector(0, 0, 0)

    for(i:Int in 0..dist.toInt()){
        locs.add(loc1.clone().add(curr))
        curr.add(dir)
    }

    return locs
}