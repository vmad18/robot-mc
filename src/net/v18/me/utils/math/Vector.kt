package net.v18.me.utils.math

class Vector(vararg elms: Number) {


    private val vect: Array<Number>

    var space: Int = elms.size

    init {
        if(elms.isEmpty()) System.exit(0)
        vect = elms.asList().toTypedArray()
    }

    operator fun get(i: Int): Double {
        if(i >= this.space) return vect[0].toDouble()
        return vect[i].toDouble()
    }

    operator fun set(i: Int, obj: Number) {
        if(i >= this.space) return
        vect[i] = obj
    }

}