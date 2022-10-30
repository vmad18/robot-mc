package net.v18.me.utils.math

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sqrt

/**
 * @param mu - the mean
 * @param sig - the std
 * @param x - input value
 **/
fun gauss_noise(mu:Number, sig:Number, x:Number): Double{

    val variance:Double = sig.toDouble()*sig.toDouble()
    val cnst:Double = 1.0/(sqrt(2 * PI * variance))
    val dist:Double = exp(-((x.toDouble() - mu.toDouble())*(x.toDouble() - mu.toDouble()))/(2*variance))

    return cnst * dist
}