package com.yhl.kotlin.spices

data class SpiceContainer(val label: String)

fun main(args: Array<String>) {
    val container1 = SpiceContainer("curry")
    val container2 = SpiceContainer("red curry")
    val container3 = SpiceContainer("pepper")
    val container4 = SpiceContainer("curry")
    println("$container1")
    println("$container2")
    println("$container3")
    println(container1 == container2)
    println(container2 == container3)
    println(container4 == container1)
    println(container4 === container1)

}