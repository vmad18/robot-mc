package net.v18.me.utils.math

import net.v18.me.utils.times
import net.v18.me.utils.todeg
import kotlin.math.*

class Quaternion(a: Number, val vec: Vec3D) {

    /**
     * Rotation Vector
     *
     * @param w - angle of rotation
     * @param v - vector of rotation
     */
    constructor(v:Vec3D, w:Number) : this(cos(w.toDouble()/2), v*sin(w.toDouble()/2))

    constructor(v:Vec3D) : this(0, v)

    var a:Double = a.toDouble()

    /*
     * Wrappers
     */

    val ic: Double
        get() = vec.x

    val jc: Double
        get() = vec.y

    val kc: Double
        get() = vec.z

    val mag: Double
        get() = sqrt(a*a + ic*ic + jc*jc + kc*kc)

    val norm: Quaternion
        get() = this*(1/mag)

    val conj: Quaternion
        get() = Quaternion(a, vec*-1)

    val reverse: Quaternion
        get() = Quaternion(a, vec.reverse)

    val roll: Double
        get() = atan2(2*(a*ic+jc*kc), 1-2*(ic*ic + jc*jc))

    val pitch: Double
        get() = asin(2*(a*jc-kc*ic))

    val yaw: Double
        get() = atan2(2*(a*kc+ic*jc), 1-2*(jc*jc + kc*kc))


    override fun toString(): String = "$a $ic $jc $kc roll: ${roll.todeg} pitch: ${pitch.todeg} yaw: ${yaw.todeg}"

    /**
     * Hamilton Product
     **/
    operator fun times(q: Quaternion): Quaternion {
        val na:Double = a*q.a - ic*q.ic - jc*q.jc - kc*q.kc
        val ni:Double = a*q.ic + ic*q.a + jc*q.kc - kc*q.jc
        val nj:Double = a*q.jc - ic*q.kc + jc*q.a + kc*q.ic
        val nk:Double = a*q.kc + ic*q.jc - jc*q.ic + kc*q.a
        return Quaternion(na, Vec3D(ni, nj, nk))
    }

    operator fun times(s: Number): Quaternion = Quaternion(s.toDouble()*a, s*vec)

}