package com.yhl.kotlin.generic

open class BaseBuildingMaterial() {
    open val numberNeeded: Int = 1

    fun <T: BaseBuildingMaterial>isSmallBuilding(building: Building<T>) {
        when {
            numberNeeded < 500 -> println("small building")
            else -> println("large building")
        }
    }
}

class Wood: BaseBuildingMaterial() {
    override val numberNeeded: Int = 4
}

class Brick: BaseBuildingMaterial() {
    override val numberNeeded: Int = 8
}