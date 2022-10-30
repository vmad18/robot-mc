package net.v18.me.utils

import net.v18.me.utils.graph.Node
import net.v18.me.utils.math.Quaternion
import net.v18.me.utils.math.Vec3D
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sqrt


fun inv_sqrt(v:Float): Float{
    var v = v
    var vhalf:Float = 0.5F * v

    var i:Int = v.toBits()
    i = 0x5f3759df - (i shl 1)
    v = i.toFloat()
    v *= (1.5F - vhalf * v * v)

    return v
}

fun particle(part: Particle, loc: Location): Unit{
    loc.world!!.spawnParticle(part, loc, 0)
}

fun particle(part: Particle, loc: ArrayList<Location>): Unit{
    loc.forEach { particle(part, it) }
}

@JvmName("particle1")
fun particle(part: Particle, verts: ArrayList<Node>): Unit{
    verts.forEach { particle(part, it.loc) }
}

fun tell(p: Player, msg: String){
    p.sendMessage(msg)
}

fun broad(str:String){
    Bukkit.broadcastMessage(str)
}


/*
fun main(){
    val test:Vec3D = RotateVector(90.toRadians, Vec3D(0, 1, 0), Vec3D(1, 0, 0))
    println("${test.to_string}")
}*/
