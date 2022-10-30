package net.v18.me.PathPlanning

import net.v18.me.Main
import net.v18.me.robot_utils.Robot
import net.v18.me.utils.broad
import net.v18.me.utils.graph.Graph
import net.v18.me.utils.graph.Node
import net.v18.me.utils.math.line
import net.v18.me.utils.particle
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import org.bukkit.util.Vector
import kotlin.collections.ArrayList
import kotlin.math.min

/**
 * @param sr - search radius
 **/
class RRTStar(private val plugin: Main, private val robot: Robot, private val start: Node, private val goal: Node, private val edgelens:ArrayList<Double> = arrayListOf(1.0, 2.0, 3.0, 5.0), private val sr:Int = 2) {

    private val graph: Graph = Graph()

    constructor(plugin: Main, robot: Robot, l1: Location, l2:Location): this(plugin, robot, Node(l1, null, 0.0), Node(l2, null, -1.0))


    /*
     * TODO remove this
     */

    private val sampled: ArrayList<Node> = ArrayList()

    private val samp_lines:(ArrayList<Node>) -> (Unit) = {
            verts:ArrayList<Node> ->
            verts.forEach { particle(Particle.FLAME, line(it.parent!!.loc, it.loc, 2)) }
    }

    init {
        graph.add_vert(start)
        //remove
        broad(start.loc.toString())
    }

    private fun heuristic(v1:Node) = v1.dist(goal)


    private val sample: Node
        get(){
            val loc_s = start.loc.clone()

            val rand = Random()

            val rX: Double = ((rand.nextInt(100) * (if(rand.nextInt(10) <= 5) 1 else -1)).toDouble())
            val rY: Double = ((rand.nextInt(100) * (if(rand.nextInt(10) <= 5) 1 else -1)).toDouble())
            val rZ: Double = ((rand.nextInt(100) * (if(rand.nextInt(10) <= 5) 1 else -1)).toDouble())

            loc_s.add(rX, rY, rZ)
            return Node(loc_s, null, -1.0)
        }


    private fun closest(v:Node): Node{
        var dist:Double = start.dist(v)+heuristic(start);
        var b_v:Node = start;

        graph.verts.forEach {
            if(it.dist(v)+heuristic(it) < dist){
                dist = it.dist(v)+heuristic(it);
                b_v = it
            }
        }

        return b_v
    }

    /**
     * @param v - Samples node
     * @param b_v - Closest node to v
     * @param e_l - Edge length
     **/
    private fun steer(v:Node, b_v:Node, e_l:Double): Node{
        var bd:Double = min(b_v.dist(v), e_l)
        val vect:Vector = v.loc.clone().toVector().subtract(b_v.loc.clone().toVector()).normalize().multiply(bd)
        val newLoc = b_v.loc.clone().add(vect)
        return Node(newLoc, b_v, e_l+b_v.cost)
    }


    /**
     * Gets the nodes with a distance of sr to v
     **/
    private fun nearest_verts(v:Node): ArrayList<Node>{
        val output = arrayListOf<Node>()
        graph.verts.forEach { if(it.dist(v) <= sr*sr) output.add(it) }
        return output
    }


    private fun opt_node(v:Node, nearest:ArrayList<Node>) {

        var best_cost:Double = v.cost
        var new_par:Node? = v.parent

        //check if the node to the potential node is possible
        nearest.forEach {
            var tcost = it.cost + it.dist(v)
            if(tcost < best_cost){
                best_cost = tcost
                new_par = it
            }
        }

        v.parent = new_par
        v.cost = best_cost
    }


    private fun rewire(v:Node, nearest:ArrayList<Node>){
        nearest.forEach {
            if(it.dist(v) + v.cost < it.cost){
                it.parent = v
                it.cost + v.cost + it.dist(v)
            }
        }
    }


    val run: Unit
        get(){

            broad("${ChatColor.DARK_PURPLE}Starting RRT*")

            object: BukkitRunnable(){
                val end_time:Long = System.currentTimeMillis() + (3.0*1000*60).toInt()
                var ind:Int = 0
                var elen:Double = edgelens[ind]
                override fun run(){
                    particle(Particle.SOUL_FIRE_FLAME, line(start.loc, goal.loc, 3))

                    var qs:Node = sample
                    var qnear: Node = closest(qs)
                    var qsteer: Node = steer(qs, qnear, 1.0)

                    var verticies: ArrayList<Node> = nearest_verts(qsteer)
                    opt_node(qsteer, verticies)
                    rewire(qsteer, verticies)



                    graph.add_vert(qsteer)

                    sampled.add(qsteer)

                    particle(Particle.SOUL_FIRE_FLAME, sampled)
                    samp_lines.invoke(sampled)

                    elen = edgelens[(++ind%edgelens.size)]
                    
                    if(System.currentTimeMillis()%3000L == 0L) {
                        broad("We are still running")
                    }

                    if(end_time <= System.currentTimeMillis()) cancel()
                }

            }.runTaskTimer(plugin, 0, 0)
        }
}