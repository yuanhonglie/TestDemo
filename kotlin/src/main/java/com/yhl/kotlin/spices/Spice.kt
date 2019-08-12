package com.yhl.kotlin.spices

abstract class Spice(var name: String, var spiciness: String = "mild", var color: SpiceColor) {

    val heat :Int
        get() {
            return when(spiciness) {
                "mild" -> 1
                "medium" -> 3
                "spicy"-> 5
                "very spicy" -> 7
                "extremely spicy" -> 10
                else -> 0
            }
        }

    abstract fun prepareSpice()

    override fun toString(): String {
        return "$name $spiciness"
    }
}

/*fun makeSalt(): Spice {
    return Spice("salt")
}*/

interface Grinder {
    fun grind()
}

interface SpiceColor {
    val color: String
}

object YellowSpiceColor : SpiceColor {
    override val color: String = "yellow"
}

class Curry(name: String, spiciness: String, color: SpiceColor = YellowSpiceColor): Spice(name, spiciness, color), Grinder{
    override fun grind() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun prepareSpice() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        grind()
    }

}

fun main(args: Array<String>) {

}