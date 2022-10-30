package net.v18.me.utils.graph

class Graph {

    private var index:Int = 0

    //verticies with their indices
    private val vert_map:HashMap<Int, Node> = HashMap()

    val verts:MutableCollection<Node>
        get() = vert_map.values

    fun add_vert(v:Node): Unit{
        vert_map[index] = v;
        index++;
    }
}