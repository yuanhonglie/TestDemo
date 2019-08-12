package com.yhl.kotlin

import java.util.*

fun main(args: Array<String>) {
    //println("hello, ${args[0]}")
    //feedTheFish()
    //dayOfWeek()
    /*
    var fortune: String
    for (i in 1..10) {
        fortune = getFortuneCookie(getBirthday())
        println("\nYour fortune is: $fortune")
        if (fortune.contains("Take it easy")) break
    }*/
    //whatShouldIDoToday("happy")
    //gamePlay(rollDice3(4))
//    val simpleSpice = SimpleSpice()
//    println("${simpleSpice.name} ${simpleSpice.heat}")
    /*
    val spices1 = listOf(
            Spice("curry", "mild"),
            Spice("paper", "medium"),
            Spice("cayenne", "spicy"),
            Spice("ginger", "mild"),
            Spice("red curry", "medium"),
            Spice("green curry", "mild"),
            Spice("hot pepper", "extremely spicy")
    )
    val spice = Spice("cayenne", spiciness = "spicy")
    val spiceList = spices1.filter { it.heat < 5 }
    println(spices1)
    println(spice)
    println(spiceList)
    println(makeSalt())
    */
}

fun getBirthday() : Int {
    print("\nEnter your birthday: ")
    return readLine()?.toIntOrNull() ?: 1
}

fun getFortuneCookie(birthday : Int) : String {
    val fortunes = listOf(
            "You will have a great day",
            "Things will go well for you today.",
            "Enjoy a wonderful day of success.",
            "Be humble and all will turn out well.",
            "Today is a good day for exercising restraint.",
            "Take it easy and enjoy life!",
            "Treasure your friends because they are your greatest fortune."
    )

    val index = when(birthday) {
        in 1..7 -> 4
        28, 31 -> 2
        else -> birthday.rem(fortunes.size)
    }

    return fortunes[index]
}

fun feedTheFish() {
    val day = "Tuesday"
    val food = "pellets"
    println("Today is $day and the fish eat $food")
}

fun dayOfWeek() {
    println("what day is today")
    var day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
    println(when(day) {
        Calendar.MONDAY -> ("Today is Monday")
        Calendar.TUESDAY -> ("Today is Tuesday")
        Calendar.WEDNESDAY -> ("Today is Wednesday")
        Calendar.THURSDAY -> ("Today is Thursday")
        Calendar.SATURDAY -> ("Today is Saturday")
        Calendar.SUNDAY -> ("Today is Sunday")
        else -> "Time has stopped"
    })
}

fun whatShouldIDoToday(mood : String, weather :String = "sunny", temperature :Int = 24) {
    println(when {
        mood == "happy" && weather == "sunny" -> "go for a walk"
        mood == "sad" && weather == "rainy" && temperature == 0 -> "stay in bed"
        temperature > 35 -> "go swimming"
        else -> "Stay home and read"
    })
}

val rollDice1 = {Random().nextInt(12) + 1}
val rollDice2 = {sides:Int -> Random().nextInt(sides) + 1}
val rollDice3 :(Int) -> Int = {sides:Int -> Random().nextInt(sides) + 1}

fun gamePlay(dicRoll: Int) {
    println(dicRoll)
}