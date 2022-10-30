package net.v18.me.utils.graph

import org.bukkit.Location

data class Node(val loc: Location, var parent:Node?, var cost:Double, val adj:ArrayList<Node> = ArrayList()){
    fun dist(n:Node) = loc.distance(n.loc)
}