package com.yhl.kotlin.generic

class Building<out T: BaseBuildingMaterial> (val material: T) {
    val baseMeterialsNeeded = 100
    val actualMaterialsNeeded: Int = baseMeterialsNeeded * material.numberNeeded

    fun build() {
        println("${material::class.simpleName} $actualMaterialsNeeded")
    }
}

fun main(args: Array<String>) {
    var wood = Wood()
    var building = Building(wood)
    building.build()
}